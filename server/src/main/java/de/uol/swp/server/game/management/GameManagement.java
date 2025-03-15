package de.uol.swp.server.game.management;

import com.google.inject.Inject;
import de.uol.swp.common.cards.data.CardType;
import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.connection.dto.DestinationInfo;
import de.uol.swp.common.game.GameActions;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.game.TransportMode;
import de.uol.swp.common.game.message.event.CardExchangeConfirmationEvent;
import de.uol.swp.common.game.message.event.SwapCardsConfirmationEvent;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.common.game.message.request.SwapCardsConfirmedRequest;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.cards.CardMapper;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.cards.data.eventcards.OnTheMoveDayAndNightEventCard;
import de.uol.swp.server.cards.data.eventcards.StateMobilizationEventCard;
import de.uol.swp.server.cards.management.CardNotFoundException;
import de.uol.swp.server.chat.ServerMessageProvider;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.connection.management.IConnectionManagement;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.GameInitializationException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.exceptions.LobbyIsEmptyException;
import de.uol.swp.server.game.states.*;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.plague.management.IPlagueManagement;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.region.management.IRegionManagement;
import de.uol.swp.server.role.Role;
import de.uol.swp.server.role.RoleRepository;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.UserMapper;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages game related operations such as creating games,
 * setting player positions, and handling card draws.
 */
public class GameManagement extends AbstractManagement implements IGameManagement {
    static final Logger LOG = LogManager.getLogger(GameManagement.class);

    private final IPlayerManagement playerManagement;
    private final ICityManagement cityManagement;
    private final IRegionManagement regionManagement;
    private final IConnectionManagement connectionManagement;
    private final IPlagueManagement plagueManagement;

    @Inject
    public GameManagement(
            IPlayerManagement playerManagement,
            ICityManagement cityManagement,
            IConnectionManagement connectionManagement,
            IRegionManagement regionManagement,
            IPlagueManagement plagueManagement
    ) {
        this.playerManagement = playerManagement;
        this.cityManagement = cityManagement;
        this.connectionManagement = connectionManagement;
        this.regionManagement = regionManagement;
        this.plagueManagement = plagueManagement;
    }

    /**
     * Creates and initializes a game based on the provided creation request.
     * It sets up the game with specified users and difficulty level, and registers it in the game store.
     *
     * @param request The request containing the necessary data to create the game
     * @return The newly created game
     */
    public IGame createAndInitializeGame(CreateGameRequest request) throws GameInitializationException {
        IGame game = new Game(request.getDifficulty(), request.getLobbyId());
        GameStore.getInstance()
                 .addGame(request.getLobbyId(), game);
        try {
            initializing(game, UserMapper.toUser(request.getUsers()));
        } catch (IllegalGameStateException e) {
            throw new GameInitializationException("Failed to initialize game", e);
        }
        return game;
    }

    /**
     * Initializes the game by setting up players, assigning roles,
     * determining the starting player, and initiating city infections.
     *
     * @param game  The game instance to initialize
     * @param users The list of users participating in the game
     * @throws IllegalGameStateException If the game is not in a state that allows players to draw cards
     */
    private void initializing(IGame game, List<IUser> users) throws IllegalGameStateException {
        initiateInfections(game);
        createPlayers(users, game);
        game.gameStartShuffle(game.getDifficulty() + 3);
        assignRoles(game);
        setStartingPlayer(game);
        game.setState(new WaitForPositioning());
    }

    /**
     * Creates player instances for the provided users, adds them to the game,
     * and assigns each player a specific number of starting cards based on the number of players.
     *
     * @param users The list of users to create players for
     * @param game  The game instance to add players to
     * @throws IllegalGameStateException If the game is not in a state that allows players to draw cards
     */
    void createPlayers(List<IUser> users, IGame game) throws IllegalGameStateException {
        for (IUser user : users) {
            Player player = new Player(user, game.getGameId());

            game.getPlayers()
                .add(player);
            int cardsToDraw = switch (users.size()) {
                case 2 -> 4;
                case 3 -> 3;
                default -> 2;
            };
            for (int i = 0; i < cardsToDraw; i++) {
                playerManagement.drawPlayerCard(game.getGameId(), player);
            }
            game.incrementCurrentPlayerIndex(users.size());
        }
    }

    /**
     * Determines the starting player based on the player holding the city card
     * with the oldest foundation date. Moves this player to the first position in the player list.
     *
     * @param game The game instance where the starting player will be set
     */

    void setStartingPlayer(IGame game) {
        int foundingDate = Integer.MAX_VALUE;
        IPlayer startingPlayer = null;
        for (IPlayer player : game.getPlayers()) {
            for (ICard card : player.getCards()) {
                if (card instanceof CityCard cityCard && cityCard.getCity()
                                                                 .getFoundationDate() < foundingDate) {
                    foundingDate = cityCard.getCity()
                                           .getFoundationDate();
                    startingPlayer = player;
                }
            }
        }
        if (startingPlayer != null) {
            game.getPlayers()
                .remove(startingPlayer);
            game.getPlayers()
                .add(0, startingPlayer);
        }
    }

    /**
     * Assigns roles to all players in the game by shuffling a list of roles
     * and distributing them sequentially to the players.
     *
     * @param game The game instance where roles will be assigned
     */
    private void assignRoles(IGame game) {
        List<Role> allRoles = RoleRepository.getAllRoles();
        Collections.shuffle(allRoles);
        for (int i = 0; i < game.getPlayers()
                                .size(); i++) {
            game.getPlayers()
                .get(i)
                .setRole(allRoles.get(i));
        }
    }

    /**
     * Initiates infections in the game by infecting cities in a predefined pattern.
     * The number of infection cubes placed decreases after every three cities.
     *
     * @param game The game instance where city infections will be initiated
     */

    private void initiateInfections(IGame game) {
        int infectionAmount = 3;
        for (int i = 1; i <= 9; i++) {
            cityManagement.infectCityWithOwnPlague(game, drawInfectionCard(game), infectionAmount);
            if (i % 3 == 0) {
                infectionAmount--;
            }
        }
    }

    /**
     * Sets the starting position of a user's player character in a game,
     * if the game is currently in a state that allows setting positioning.
     * Updates the game state if all players have been positioned.
     *
     * @param request The request with where the position is to be set
     * @throws GameException             If the game is not in a state that allows setting positioning
     * @throws IllegalGameStateException If the game is not in a state that allows setting positioning
     */
    public void setPositioning(PositioningRequest request) throws GameException, IllegalGameStateException {
        IGame game = getGame(request.getLobbyId());

        if (!(game.getState() instanceof WaitForPositioning waitForPositioningState)) {
            LOG.error("[LobbyID: {}] Game is not in a state that allows setting positioning", game.getGameId());
            throw new IllegalGameStateException("Game is not in a state that allows setting positioning");
        }

        List<IPlayer> players = game.getPlayers();
        IPlayer requestPlayer = null;
        for (IPlayer player : players) {
            if (player.getUser()
                      .getUsername()
                      .equals(request.getSession()
                                     .orElseThrow(() -> new SessionNotFoundException("Session not found"))
                                     .getUser()
                                     .getUsername())) {
                requestPlayer = player;
                break;
            }
        }

        try {
            assert requestPlayer != null;
            if (requestPlayer.getCurrentPosition() != null) {
                return;
            }
            playerManagement.setStartingPosition(
                    game.getGameId(),
                    game.getCityRepository()
                        .getCityNameById(request.getCityId()),
                    requestPlayer
            );
            waitForPositioningState.setPositionedPlayersCount(waitForPositioningState.getPositionedPlayersCount() + 1);
            sendServerMessageEvent(
                    game.getGameId(),
                    requestPlayer.getUser()
                                 .getUsername() + " startet von " + game.getCityRepository()
                                                                        .getCityNameById(request.getCityId())
                                                                        .getDisplayName()
            );
        } catch (PlayerManagementException e) {
            throw new GameException("Failed to set Position");
        }
        if (game.getState() instanceof WaitForPositioning && waitForPositioningState.getPositionedPlayersCount() == game.getPlayers()
                                                                                                                        .size()) {
            game.setState(new PlayerTurnState());
            game.setCurrentPlayerIndex(0);
        }
    }

    /**
     * Draws an infection card from the deck.
     *
     * @return The drawn infection card
     */
    public InfectionCard drawInfectionCard(IGame game) {
        List<InfectionCard> infectionCardDrawPile = game.getInfectionCardDrawPile();

        if (infectionCardDrawPile.isEmpty()) {
            throw new IllegalStateException("Infection card draw pile is empty");
        }
        InfectionCard card;
        if (game.getState() instanceof InfectionState infectionState) {
            if (game.isFavorableTimeEventCardPlayed()) {
                card = infectionCardDrawPile.remove(infectionCardDrawPile.size() - 1);
                infectionState.setInfectedCitiesToOnlyDrawOneMoreCard(game);
                game.setFavorableTimeEventCardPlayed(false);
            } else {
                card = infectionCardDrawPile.remove(0);
            }
        } else if (game.getState() instanceof StartState) {
            return infectionCardDrawPile.remove(0);
        } else {
            LOG.error(
                    "[LobbyID: {}] Failed to draw infection card. Game is not in a state that allows drawing infection cards",
                    game.getGameId()
            );
            return null;
        }
        cityManagement.infectCityWithOwnPlague(game, card, 1);
        return null;
    }

    /**
     * Discards an infection card by adding it to the infection card discard pile of the specified game.
     *
     * @param game          The game from which the infection card is to be discarded
     * @param infectionCard The infection card to be discarded
     */
    public void discardInfectionCard(IGame game, InfectionCard infectionCard) {
        List<InfectionCard> infectionCardDiscardPile = game.getInfectionCardDiscardPile();

        infectionCardDiscardPile.add(infectionCard);
    }

    public List<GameActions> getAvailableActions(String lobbyId, IUser user) {
        List<GameActions> actions = new ArrayList<>();
        if (areTrainTracksBuildable(lobbyId)) {
            actions.add(GameActions.BUILD_TRAIN_TRACKS);
        }
        if (cityManagement.isHospitalBuildable(lobbyId, user.getUsername())) {
            actions.add(GameActions.BUILD_HOSPITAL);
        }
        if (isKnowledgeShareable(lobbyId)) {
            actions.add(GameActions.SHARE_KNOWLEDGE);
        }
        if (isInfectionTreatable(lobbyId)) {
            actions.add(GameActions.TREAT_INFECTION);
        }
        if (isPlagueResearchable(lobbyId)) {
            actions.add(GameActions.RESEARCH_PLAGUE);
        }
        if (isWaterTreatmentPlaceable(lobbyId, user)) {
            actions.add(GameActions.TREAT_WATER);
        }
        if (roleActionOneAvailable(lobbyId)) {
            actions.add(GameActions.ROLE_ACTION_ONE);
        }
        if (roleActionTwoAvailable(lobbyId)) {
            actions.add(GameActions.ROLE_ACTION_TWO);
        }
        if (isEndTurnPossible(lobbyId, user)) {
            actions.add(GameActions.END_TURN);
        }
        return actions;
    }

    /**
     * Checks if the first role action is available for the current player.
     *
     * @param lobbyCode The code of the lobby
     * @return true if the first role action is available, false otherwise
     */
    private boolean roleActionOneAvailable(String lobbyCode) {
        IGame game = getGame(lobbyCode);
        if (game.getCurrentPlayer()
                .getRole()
                .getName()
                .equals(RoleEnum.POLITICIAN)) {
            return game.getCurrentPlayer()
                       .getCards()
                       .stream()
                       .anyMatch(iCard -> iCard.getId() == game.getCurrentPlayer()
                                                               .getCurrentPosition()
                                                               .getId());
        } else if (game.getCurrentPlayer()
                       .getRole()
                       .getName()
                       .equals(RoleEnum.SCIENTIST_OF_THE_ROYAL_ACADEMY)) {
            return !game.getPlayerCardDrawPile()
                        .isEmpty();
        }
        return false;
    }

    /**
     * Checks if the second role action is available for the current player.
     * This action is specific to the Politician role.
     *
     * @param lobbyCode The code of the lobby
     * @return true if the second role action is available, false otherwise
     */
    private boolean roleActionTwoAvailable(String lobbyCode) {
        if (getGame(lobbyCode).getCurrentPlayer()
                              .getRole()
                              .getName()
                              .equals(RoleEnum.POLITICIAN)) {
            boolean playerHasCurrentCityCard = getGame(lobbyCode).getCurrentPlayer()
                                                                 .getCards()
                                                                 .stream()
                                                                 .anyMatch(card -> card.getId() == getGame(lobbyCode).getCurrentPlayer()
                                                                                                                     .getCurrentPosition()
                                                                                                                     .getId());

            boolean currentCityCardIsOnDiscardPile = getGame(lobbyCode).getPlayerCardDiscardPile()
                                                                       .stream()
                                                                       .anyMatch(card -> card.getId() == getGame(
                                                                               lobbyCode).getCurrentPlayer()
                                                                                         .getCurrentPosition()
                                                                                         .getId());

            boolean playerHasAnyCityCard = getGame(lobbyCode).getCurrentPlayer()
                                                             .getCards()
                                                             .stream()
                                                             .anyMatch(card -> card instanceof CityCard);

            boolean discardPileHasAnyCard = !getGame(lobbyCode).getPlayerCardDiscardPile()
                                                               .isEmpty();

            return (playerHasCurrentCityCard && discardPileHasAnyCard) || (playerHasAnyCityCard && currentCityCardIsOnDiscardPile);
        }
        return false;
    }

    /**
     * Checks if train tracks can be built in the specified lobby.
     * If the current player is in a city where train tracks can be built and has enough tracks left, the action is possible.
     *
     * @param lobbyId the code of the lobby
     * @return true if train tracks can be built, false otherwise
     */
    private boolean areTrainTracksBuildable(String lobbyId) {
        IGame game = super.getGame(lobbyId);
        IPlayer player = game.getCurrentPlayer();
        if (connectionManagement.getBuildableTrainTracks(
                                        lobbyId,
                                        player.getCurrentPosition()
                                              .getId()
                                )
                                .isEmpty()) {
            return false;
        }

        return game.getTracksLeft() >= 0;
    }

    /**
     * Checks if knowledge can be shared in the current game state.
     *
     * @param lobbyCode The code of the lobby where the game is being played
     * @return true if knowledge can be shared, false otherwise
     */
    private boolean isKnowledgeShareable(String lobbyCode) {
        IGame game = this.getGame(lobbyCode);
        IPlayer currentPlayer = game.getCurrentPlayer();
        int currentCityId = currentPlayer.getCurrentPosition()
                                         .getId();


        long playersInCity = game.getPlayers()
                                 .stream()
                                 .filter(player -> player.getCurrentPosition()
                                                         .getId() == currentCityId)
                                 .count();

        if (playersInCity < 2) {
            return false;
        }

        return game.getPlayers()
                   .stream()
                   .filter(player -> player.getCurrentPosition()
                                           .getId() == currentCityId)
                   .anyMatch(player -> player.getCards()
                                             .stream()
                                             .filter(CityCard.class::isInstance)
                                             .map(CityCard.class::cast)
                                             .anyMatch(card -> card.getCity()
                                                                   .getId() == currentCityId));
    }

    /**
     * Checks if an infection is treatable in the current city of the current player.
     *
     * @param lobbyCode The code of the lobby
     * @return true if there is an infection with severity greater than 0, false otherwise
     */
    private boolean isInfectionTreatable(String lobbyCode) {
        ICity city = getGame(lobbyCode).getCurrentPlayer()
                                       .getCurrentPosition();
        return city.getInfections()
                   .stream()
                   .anyMatch(infection -> infection.getSeverity() > 0);
    }

    /**
     * Checks if the plague is researchable in the specified game.
     *
     * @param lobbyId The ID of the lobby
     * @return true if the plague can be researched, false otherwise
     */
    private boolean isPlagueResearchable(String lobbyId) {
        IGame game = getGame(lobbyId);
        return plagueManagement.canResearchPlague(game);
    }

    /**
     * Checks if a water treatment can be placed in the specified game.
     *
     * @param lobbyCode The code of the lobby
     * @param user      The user requesting the water treatment placement
     * @return true if there are available regions for water treatment, false otherwise
     */
    boolean isWaterTreatmentPlaceable(String lobbyCode, IUser user) {
        IGame game = getGame(lobbyCode);
        Set<IRegionDTO> availableRegions = new HashSet<>();
        if (game.getWaterTreatmentsLeft() > 0) {
            availableRegions = regionManagement.getAvailableRegions(UserMapper.toDTO(user), lobbyCode);
        }
        return !availableRegions.isEmpty();
    }

    /**
     * Checks if the current player can end their turn in the specified lobby.
     *
     * @param lobbyCode the code of the lobby
     * @param user      the user for whom the check is performed
     * @return true if the current player can end their turn, false otherwise
     */
    private boolean isEndTurnPossible(String lobbyCode, IUser user) {
        IGame game = getGame(lobbyCode);
        return game.getCurrentPlayer()
                   .getUser()
                   .equals(user) && game.getState() instanceof PlayerTurnState;
    }

    @Override
    public void endTurn(String lobbyId, IUser user) throws IllegalGameStateException {
        IGame game = getGame(lobbyId);
        if (game.getCurrentPlayer()
                .getUser()
                .equals(user) && game.getState() instanceof PlayerTurnState) {
            LOG.info("[LobbyID: {}] Ending turn for player {}", lobbyId, user.getUsername());
            game.setState(new DrawCardState());
        } else {
            throw new IllegalGameStateException("Player is not allowed to end turn");
        }
    }

    @Override
    public void movePlayer(
            IUser user,
            String lobbyId,
            ICity city,
            ICard card
    ) throws GameException, IllegalGameStateException {
        IGame game = super.getGame(lobbyId);

        IGameState gameState = game.getState();
        validateGameStateForMove(lobbyId, gameState);

        IPlayer player = getPlayerForMove(game, user);

        Map<Integer, DestinationInfo> availableDestinations = retrieveAvailableDestinations(game, player);
        boolean citiesConnectedByLand = availableDestinations.containsKey(city.getId()) && (availableDestinations.get(
                                                                                                                         city.getId())
                                                                                                                 .getTransportModes()
                                                                                                                 .contains(
                                                                                                                         TransportMode.CARRIAGE) || availableDestinations.get(
                                                                                                                                                                                 city.getId())
                                                                                                                                                                         .getTransportModes()
                                                                                                                                                                         .contains(
                                                                                                                                                                                 TransportMode.TRAIN) || availableDestinations.get(
                                                                                                                                                                                                                                      city.getId())
                                                                                                                                                                                                                              .getTransportModes()
                                                                                                                                                                                                                              .contains(
                                                                                                                                                                                                                                      TransportMode.NONE));
        boolean citiesConnectedBySea = availableDestinations.containsKey(city.getId()) && availableDestinations.get(city.getId())
                                                                                                               .getTransportModes()
                                                                                                               .contains(
                                                                                                                       TransportMode.SHIP);

        if (!citiesConnectedByLand && !citiesConnectedBySea) {
            LOG.error(
                    "[LobbyID: {}] Failed to move {}. There is no available connection between {} and {}",
                    lobbyId,
                    player.getUser()
                          .getUsername(),
                    player.getCurrentPosition()
                          .getName()
                          .getDisplayName(),
                    city.getName()
                        .getDisplayName()
            );
            throw new GameException("There is no available connection between " + player.getCurrentPosition()
                                                                                        .getName()
                                                                                        .getDisplayName() + " and " + city.getName()
                                                                                                                          .getDisplayName());
        }

        if (citiesConnectedByLand) {
            String serverMessage = availableDestinations.get(city.getId())
                                                        .getTransportModes()
                                                        .contains(TransportMode.TRAIN) ? ServerMessageProvider.trainMessage(player,
                    city
            ) : ServerMessageProvider.carriageMessage(player, city);
            sendServerMessageEvent(lobbyId, serverMessage);
            movePlayerByLand(game, player, city);
        } else {
            sendServerMessageEvent(lobbyId, ServerMessageProvider.sailMessage(player, city));
            movePlayerBySea(game, player, city, card);
        }
    }


    /**
     * Validates if the game states allows moving players.
     *
     * @param lobbyId   the ID of the lobby
     * @param gameState the current game state
     * @throws IllegalGameStateException when the game state does not allow moving players
     */
    private void validateGameStateForMove(String lobbyId, IGameState gameState) throws IllegalGameStateException {
        if (!(gameState instanceof PlayerTurnState) && !(gameState instanceof EventState)) {
            LOG.error("[LobbyID: {}] Game is not in a state that allows moving players", lobbyId);
            throw new IllegalGameStateException("Game is not in a state that allows moving players");
        }
    }

    /**
     * Retrieves the player for move.
     * When the game is in PlayerTurnState the current player will be returned.
     * When the game is in EventState the player with the given user will be returned.
     *
     * @param game the game instance
     * @param user the user requesting the move
     * @return the player for the move
     * @throws GameException when the user requesting the move is not the current player
     */
    private IPlayer getPlayerForMove(IGame game, IUser user) throws GameException {
        IPlayer player = game.getState() instanceof PlayerTurnState ? game.getCurrentPlayer() : game.getPlayer(user.getUsername());
        if (game.getState() instanceof PlayerTurnState && !player.getUser()
                                                                 .equals(user)) {
            LOG.error("[LobbyID: {}] {} is not the current player", game.getGameId(), user.getUsername());
            throw new GameException("Player is not the current player");
        }
        return player;
    }

    /**
     * Retrieves the available destinations for the player.
     * When the event card on the move day and night has been played all destinations will be returned.
     * Else only the available destinations from the current position will be returned.
     *
     * @param game   the game instance
     * @param player the player requesting the move
     * @return a map of available destinations
     */
    private Map<Integer, DestinationInfo> retrieveAvailableDestinations(IGame game, IPlayer player) {
        if (game.getState() instanceof EventState eventState && eventState.getEventCard() instanceof OnTheMoveDayAndNightEventCard) {
            return connectionManagement.getAllDestinations(game.getGameId());
        }
        return connectionManagement.getAvailableDestinations(
                game.getGameId(),
                player.getUser()
                      .getUsername()
        );
    }

    /**
     * Moves the player to the specified city by land.
     *
     * @param game   the game instance
     * @param player the player to move
     * @param city   the destination city
     */
    private void movePlayerByLand(IGame game, IPlayer player, ICity city) {
        LOG.debug(
                "[LobbyID: {}] Moving {} to city {}",
                game.getGameId(),
                player.getUser()
                      .getUsername(),
                city.getName()
                    .getDisplayName()
        );
        this.setPlayerPosition(game, player, city);
    }

    /**
     * Moves the player to the specified city by sea.
     * If necessary the given card will be discarded.
     *
     * @param game   the game instance
     * @param player the player to move
     * @param city   the destination harbour city
     * @param card   the card used for the move
     */
    private void movePlayerBySea(IGame game, IPlayer player, ICity city, ICard card) throws GameException {
        boolean playerIsSailor = player.getRole()
                                       .getName()
                                       .equals(RoleEnum.SAILOR);
        if (!playerIsSailor) {
            playerManagement.discardPlayerCard(
                    game.getGameId(),
                    player.getUser()
                          .getUsername(),
                    card.getId()
            );
        }

        LOG.debug(
                "[LobbyID: {}] {} sails to {}",
                game.getGameId(),
                player.getUser()
                      .getUsername(),
                city.getName()
                    .getDisplayName()
        );
        this.setPlayerPosition(game, player, city);
    }

    /**
     * Sets the player's position to the specified city.
     * If the game is in the PlayerTurnState the player's actions remaining will be reduced.
     * If the game is in the EventState and the event card is a StateMobilizationEventCard the players to move will be reduced.
     * If the event has been resolved the game will be set to the previous state.
     *
     * @param game   the game instance
     * @param player the player to move
     * @param city   the destination city
     */
    private void setPlayerPosition(IGame game, IPlayer player, ICity city) {
        LOG.debug(
                "[LobbyId: {}] Setting {}'s position to {}",
                game.getGameId(),
                player.getUser()
                      .getUsername(),
                city.getName()
                    .getDisplayName()
        );

        if (game.getState() instanceof PlayerTurnState playerTurnState) {
            playerTurnState.reduceActionsRemaining(game);
            LOG.info("[LobbyId: {}] Decreased actions remaining", game.getGameId());
            player.setCurrentPosition(city);
            LOG.info("[LobbyId: {}] Player has been moved", game.getGameId());
        } else if (game.getState() instanceof EventState eventState) {
            player.setCurrentPosition(city);
            LOG.info("[LobbyId: {}] Player has been moved", game.getGameId());
            if (eventState.getEventCard() instanceof StateMobilizationEventCard stateMobilizationEventCard) {
                LOG.info("[LobbyId: {}] Decreasing players to move.", game.getGameId());
                stateMobilizationEventCard.playerMoved(player);
                if (!stateMobilizationEventCard.getPlayersToMove()
                                               .isEmpty()) {
                    LOG.debug(
                            "[LobbyId: {}] Decreased players to move. {} players left to move.",
                            game.getGameId(),
                            stateMobilizationEventCard.getPlayersToMove()
                    );
                    return;
                }
            }
            game.setState(game.getPreviousState());
            LOG.info("[LobbyId: {}] Event has been resolved. Setting game to previous state", game.getGameId());
        }
    }

    @Override
    public void buildTrainTrack(
            IUser user,
            String lobbyId,
            IConnection connection
    ) throws IllegalGameStateException, GameException {
        IGame game = super.getGame(lobbyId);

        IGameState gameState = game.getState();
        if (!(gameState instanceof PlayerTurnState)) {
            LOG.error("[LobbyID: {}] Game is not in a state that allows to build train tracks", lobbyId);
            throw new IllegalGameStateException("Game is not in a state that allows to build train tracks");
        }

        IPlayer player = game.getCurrentPlayer();
        if (!player.getUser()
                   .equals(user)) {
            LOG.error("[LobbyID: {}] {} is not the current player", lobbyId, user.getUsername());
            throw new GameException("Player is not the current player");
        }

        RoleEnum roleName = game.getCurrentPlayer()
                                .getRole()
                                .getName();

        List<IConnection> buildableTrainTracks = getBuildableTrainTracks(lobbyId, game, player);

        if (!buildableTrainTracks.contains(connection)) {
            LOG.error(
                    "[LobbyID: {}] Failed to build train track. Connection between {} and {} is not buildable",
                    lobbyId,
                    connection.getCityNames()
                              .get(0),
                    connection.getCityNames()
                              .get(1)
            );
            throw new GameException("Connection between " + connection.getCityNames()
                                                                      .get(0) + " and " + connection.getCityNames()
                                                                                                    .get(1) + " is not buildable");
        }

        game.getConnectionRepository()
            .getConnectionByID(connection.getId())
            .buildTrainTracks(true);
        game.setTracksLeft(game.getTracksLeft() - 1);
        LOG.debug(
                "[LobbyID: {}] {} builds train track between {} and {}",
                lobbyId,
                player.getUser()
                      .getUsername(),
                connection.getCityNames()
                          .get(0),
                connection.getCityNames()
                          .get(1)
        );

        if (roleName == RoleEnum.RAILWAY_PERSON) {
            if (game.getState() instanceof PlayerTurnState playerTurnState && !(playerTurnState instanceof BuildExtraTrainTrackState)) {
                CityName cityName = connection.getCityNames()
                                              .stream()
                                              .filter(name -> !name.equals(player.getCurrentPosition()
                                                                                 .getName()))
                                              .findFirst()
                                              .orElseThrow(() -> new GameException("Error while building train track"));

                List<IConnection> buildableExtraTrainTracks = connectionManagement.getBuildableTrainTracks(
                        lobbyId,
                        cityName.getId()
                );

                if (!buildableExtraTrainTracks.isEmpty()) {
                    game.setState(new BuildExtraTrainTrackState(
                            buildableExtraTrainTracks,
                            playerTurnState.getActionsRemaining()
                    ));
                }
            } else {
                game.setState(game.getPreviousState());
                ((PlayerTurnState) game.getState()).reduceActionsRemaining(game);
            }
        } else {
            ((PlayerTurnState) game.getState()).reduceActionsRemaining(game);
        }
    }

    /**
     * Retrieves a list of buildable train tracks for the current player in the specified game.
     * <p>
     * If the game is in the BuildExtraTrainTrackState, it returns the connections from that state.
     * Otherwise, it fetches the buildable train tracks based on the player's current position.
     *
     * @param lobbyId The ID of the lobby
     * @param game    The game instance
     * @param player  The current player
     * @return A list of buildable train tracks
     */
    private List<IConnection> getBuildableTrainTracks(String lobbyId, IGame game, IPlayer player) {
        List<IConnection> buildableTrainTracks;
        if (game.getState() instanceof BuildExtraTrainTrackState state) {
            buildableTrainTracks = state.getConnections();
        } else {
            buildableTrainTracks = connectionManagement.getBuildableTrainTracks(
                    lobbyId,
                    player.getCurrentPosition()
                          .getId()
            );
        }
        return buildableTrainTracks;
    }

    public void lockGameInWaitForConfirmation(String lobbyId) {
        IGame game = getGame(lobbyId);
        WaitForConfirmationState waitForConfirmationState = new WaitForConfirmationState();
        game.setState(waitForConfirmationState);
        LOG.debug("[LobbyID: {}] Game locked in wait-for-confirmation state", lobbyId);
    }

    public void unlockGameInWaitForConfirmation(String lobbyId) {
        IGame game = getGame(lobbyId);
        game.setState(game.getPreviousState());
        LOG.debug("[LobbyID: {}] Game unlocked from wait-for-confirmation state", lobbyId);
    }


    /**
     * Increases the number of actions remaining for the current player in the game.
     * <p>
     * This method checks if the current state or the previous state of the game is an instance of
     * {@link PlayerTurnState}. If so, it increases the actions remaining for the player by the specified amount.
     *
     * @param game   the game instance where the player's actions are to be increased
     * @param amount the amount by which to increase the actions remaining
     */
    public void increaseCurrentPlayerActions(IGame game, int amount) {
        PlayerTurnState playerTurnState = null;
        if (game.getState() instanceof PlayerTurnState state) {
            playerTurnState = state;
        } else if (game.getPreviousState() instanceof PlayerTurnState state) {
            playerTurnState = state;
        }
        if (playerTurnState != null) {
            LOG.debug("Current player´s actions increased by {}", amount);
            playerTurnState.setActionsRemaining(playerTurnState.getActionsRemaining() + amount);
        }
    }


    public void removePlayer(String lobbyId, IUser user) throws LobbyIsEmptyException {
        IGame game = getGame(lobbyId);
        IPlayer player = game.getPlayer(user.getUsername());
        LOG.info("[LobbyID: {}] Player {} has left the game", lobbyId, user.getUsername());

        if (game.getPlayers()
                .size() == 1) {
            this.removeGame(game.getGameId());
            throw new LobbyIsEmptyException("Lobby is empty");
        } else if (game.getPlayers()
                       .size() <= 2) {
            LOG.info("[LobbyID: {}] Game has ended due to insufficient players", lobbyId);
            sendServerMessageEvent(
                    lobbyId,
                    user.getUsername() + " hat das Spiel verlassen. Das Spiel wurde aufgrund von zu wenigen Spielern beendet"
            );

            game.getPlayers()
                .remove(player);
            if (!(game.getState() instanceof EndGameState)) {
                game.setState(new EndGameState(false));
            }
        } else {
            for (ICard card : player.getCards()) {
                game.getPlayerCardDiscardPile()
                    .add(card);
            }
            player.getCards()
                  .clear();

            LOG.info("[LobbyID: {}] Cards of Player {} have been discarded", lobbyId, user.getUsername());
            sendServerMessageEvent(
                    lobbyId,
                    user.getUsername() + " hat das Spiel verlassen. Die Karten von " + user.getUsername() + " wurden abgelegt"
            );

            game.getPlayers()
                .remove(player);
        }
    }

    public void removeGame(String lobbyId) {
        LOG.info("[LobbyID: {}] Game has been removed from game store", lobbyId);
        GameStore.getInstance()
                 .removeGame(lobbyId);
    }

    @Override
    public void giveCard(CardExchangeConfirmationEvent response) {
        IGame game = getGame(response.getLobbyId());

        IPlayer receivingCardPlayer = game.getPlayer(response.getReceivingCardPlayer()
                                                             .getUsername());
        IPlayer givingCardPlayer = game.getPlayer(response.getGivingCardPlayer()
                                                          .getUsername());

        try {
            ICard requestCard = playerManagement.getCard(
                    response.getLobbyId(),
                    givingCardPlayer.getUser()
                                    .getUsername(),
                    response.getRequestingCard()
                            .getId()

            );

            receivingCardPlayer.getCards()
                               .add(requestCard);
            givingCardPlayer.getCards()
                            .remove(requestCard);
            ((PlayerTurnState) game.getState()).reduceActionsRemaining(game);
            sendServerMessageEvent(
                    response.getLobbyId(),
                    "Spieler " + receivingCardPlayer.getUser()
                                                    .getUsername() + " hat die Karte " + requestCard.getTitle() + " von " + "Spieler " + givingCardPlayer.getUser()
                                                                                                                                                         .getUsername() + " bekommen."
            );

        } catch (PlayerManagementException e) {
            new StatusResponse(response.getLobbyId(), false, "Player not found");
        }


    }


    @Override
    public Map<String, List<ICardDTO>> getAvailableCardsForPoliticianSecondRoleAction(String lobbyId) {
        Map<String, List<ICardDTO>> returnMap = new HashMap<>();
        IGame game = getGame(lobbyId);
        IPlayer player = game.getCurrentPlayer();

        if (player.getCards()
                  .stream()
                  .anyMatch(iCard -> iCard.getId() == player.getCurrentPosition()
                                                            .getId())) {
            List<ICardDTO> cardOfCurrentCity = CardMapper.toMixedCardDTOList(player.getCards()
                                                                                   .stream()
                                                                                   .filter(iCard -> iCard.getId() == player.getCurrentPosition()
                                                                                                                           .getId())
                                                                                   .collect(Collectors.toList()));
            returnMap.put(
                    player.getUser()
                          .getUsername(), cardOfCurrentCity
            );
            List<ICardDTO> cityCardsInDiscardPile = new ArrayList<>();
            game.getPlayerCardDiscardPile()
                .stream()
                .filter(ICard -> ICard.getType()
                                      .equals(CardType.CITY_CARD))
                .forEach(card -> cityCardsInDiscardPile.add(CardMapper.toDTO(card)));

            returnMap.put("Ablagestapel", cityCardsInDiscardPile);

        } else {

            returnMap.put(
                    player.getUser()
                          .getUsername(),
                    CardMapper.toMixedCardDTOList(player.getCards()
                                                        .stream()
                                                        .filter(card -> card instanceof CityCard)
                                                        .collect(Collectors.toList()))
            );

            returnMap.put(
                    "Ablagestapel",
                    CardMapper.toMixedCardDTOList(game.getPlayerCardDiscardPile()
                                                      .stream()
                                                      .filter(iCard -> iCard.getId() == player.getCurrentPosition()
                                                                                              .getId())
                                                      .collect(Collectors.toList()))
            );
        }

        return returnMap;
    }

    @Override
    public void swapCardsWithDiscardPile(Map<String, ICardDTO> cards, String lobbyId) {
        IGame game = getGame(lobbyId);
        IPlayer player = game.getCurrentPlayer();
        ICard cardToDiscard = player.getCard(cards.get(player.getUser()
                                                             .getUsername())
                                                  .getId());
        try {
            ICard cardFromDiscardPile = game.getPlayerCardDiscardPile()
                                            .stream()
                                            .filter(iCard -> iCard.getId() == cards.get("Ablagestapel")
                                                                                   .getId())
                                            .findFirst()
                                            .orElseThrow(() -> new CardNotFoundException(
                                                    "Card not found in discard pile"));

            player.getCards()
                  .add(cardFromDiscardPile);
            player.getCards()
                  .remove(cardToDiscard);
            game.getPlayerCardDiscardPile()
                .remove(cardFromDiscardPile);
            game.getPlayerCardDiscardPile()
                .add(cardToDiscard);
            ((PlayerTurnState) game.getState()).reduceActionsRemaining(game);

            sendServerMessageEvent(
                    lobbyId,
                    "Player " + player.getUser()
                                      .getUsername() + " hat die Karte " + cardToDiscard.getTitle() + " mit der Karte " + cardFromDiscardPile.getTitle() + "aus dem Ablagestapel getauscht"
            );


        } catch (CardNotFoundException e) {
            LOG.error("Card not found");

        }
    }

    @Override
    public void swapCards(SwapCardsConfirmedRequest request) {
        IGame game = getGame(request.getLobbyId());
        SwapCardsConfirmationEvent event = request.getEvent();
        IPlayer requestingPlayer = game.getPlayer(event.getRequestingPlayer());
        IPlayer confirmingPlayer = game.getPlayer(event.getTargetPlayer());
        ICard requestingCard = requestingPlayer.getCard(event.getRequestingPlayerCard()
                                                             .getId());
        ICard confirmingPlayerCard = confirmingPlayer.getCard(event.getOtherPlayerCard()
                                                                   .getId());

        requestingPlayer.getCards()
                        .add(confirmingPlayerCard);
        confirmingPlayer.getCards()
                        .remove(confirmingPlayerCard);
        confirmingPlayer.getCards()
                        .add(requestingCard);
        requestingPlayer.getCards()
                        .remove(requestingCard);
        sendServerMessageEvent(
                request.getLobbyId(),
                "Spieler " + event.getRequestingPlayer() + " und " + event.getTargetPlayer() + "haben " + "Karten getauscht"
        );


    }
}
