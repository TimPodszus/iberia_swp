package de.uol.swp.server.game.management;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.GameActions;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.cards.data.eventcards.AnotherDayEventCard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.connection.management.IConnectionManagement;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.states.BuildExtraTrainTrackState;
import de.uol.swp.server.game.states.IGameState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.WaitForConfirmationState;
import de.uol.swp.server.game.states.WaitForPositioning;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.role.Role;
import de.uol.swp.server.role.RoleRepository;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;

import java.util.*;

/**
 * Manages game related operations such as creating games,
 * setting player positions, and handling card draws.
 */
public class GameManagement extends AbstractManagement implements IGameManagement {
    static final Logger LOG = LogManager.getLogger(GameManagement.class);

    private final IPlayerManagement playerManagement;
    private final ICityManagement cityManagement;
    private final IConnectionManagement connectionManagement;

    @Inject
    public GameManagement(IPlayerManagement playerManagement, ICityManagement cityManagement, IConnectionManagement connectionManagement) {
        this.playerManagement = playerManagement;
        this.cityManagement = cityManagement;
        this.connectionManagement = connectionManagement;
    }

    /**
     * Creates and initializes a game based on the provided creation request.
     * It sets up the game with specified users and difficulty level, and registers it in the game store.
     *
     * @param request The request containing the necessary data to create the game
     * @return The newly created game
     */
    public IGame createAndInitializeGame(CreateGameRequest request) {
        IGame game = new Game(request.getDifficulty(), request.getLobbyId());
        GameStore.getInstance()
                 .addGame(request.getLobbyId(), game);
        try {
            initializing(game, UserMapper.toUser(request.getUsers()));
        } catch (PlayerManagementException e) {
            //TODO: irgendwo Fehler anzeigen "Fehler beim Initialisieren des Spiels"
        }
        return game;
    }

    /**
     * Initializes the game by setting up players, assigning roles,
     * determining the starting player, and initiating city infections.
     *
     * @param game  The game instance to initialize
     * @param users The list of users participating in the game
     */
    private void initializing(IGame game, List<IUser> users) throws PlayerManagementException {
        initiateInfections(game);
        createPlayers(users, game);
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
     */
    private void createPlayers(List<IUser> users, IGame game) throws PlayerManagementException {
        for (IUser user : users) {
            Player player = new Player(user);

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
            int currentPlayerIndex = game.getCurrentPlayerIndex();
            int nextPlayerIndex = currentPlayerIndex == users.size() - 1 ? 0 : currentPlayerIndex + 1;
            game.setCurrentPlayerIndex(nextPlayerIndex);
        }

    }

    /**
     * Determines the starting player based on the player holding the city card
     * with the oldest foundation date. Moves this player to the first position in the player list.
     *
     * @param game The game instance where the starting player will be set
     */

    private void setStartingPlayer(IGame game) {
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
     */
    public IGame setPositioning(PositioningRequest request) throws GameManagementException {
        IGame game = getGame(request.getLobbyId());

        if (game == null) {
            throw new GameManagementException("Game not found");
        }

        if (game.getState() instanceof WaitForPositioning waitForPositioning) {
            List<IPlayer> players = game.getPlayers();
            IPlayer requestPlayer = null;
            for (IPlayer player : players) {
                if (player.getUser()
                          .getUsername()
                          .equals(request.getSession()
                                         .orElseThrow(() -> new GameManagementException("Session not found"))
                                         .getUser()
                                         .getUsername())) {
                    requestPlayer = player;
                    break;
                }
            }
            try {
                assert requestPlayer != null;
                if(requestPlayer.getCurrentPosition() != null) {
                    throw new GameManagementException("Player is already positioned");
                }
                playerManagement.setStartingPosition(
                        game.getGameId(),
                        game.getCityRepository()
                            .getCityNameById(request.getCityId()),
                        requestPlayer
                );
                waitForPositioning.setPositionedPlayersCount(waitForPositioning.getPositionedPlayersCount() + 1);
            } catch (PlayerManagementException e) {
                throw new GameManagementException("Failed to set Position");
            }
            if (waitForPositioning.getPositionedPlayersCount() == game.getPlayers()
                                                                      .size()) {
                game.setState(new PlayerTurnState());
                game.setCurrentPlayerIndex(0);
            }
        } else {
            throw new GameManagementException("Game is not in a state that allows setting positioning");
        }
        return game;
    }

    /**
     * Draws an infection card from the deck.
     *
     * @return The drawn infection card, or null if no card can be drawn
     */
    public InfectionCard drawInfectionCard(IGame game) {
        List<InfectionCard> infectionCardDrawPile = game.getInfectionCardDrawPile();

        if (infectionCardDrawPile.isEmpty()) {
            throw new IllegalStateException("Infection card draw pile is empty");
        }

        return infectionCardDrawPile.remove(0);
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

    public List<GameActions> getAvailableActions(String lobbyCode, IUser user) {
        List<GameActions> actions = new ArrayList<>();
        if (areTrainTracksBuildable(lobbyCode)) {
            actions.add(GameActions.BUILD_TRAIN_TRACKS);
        }
        if (isHospitalBuildable()) {
            actions.add(GameActions.BUILD_HOSPITAL);
        }
        if (isKnowledgeShareable()) {
            actions.add(GameActions.SHARE_KNOWLEDGE);
        }
        if (isInfectionTreatable()) {
            actions.add(GameActions.TREAT_INFECTION);
        }
        if (isPlagueResearchable()) {
            actions.add(GameActions.RESEARCH_PLAGUE);
        }
        if (isWaterTreatmentPlaceable()) {
            actions.add(GameActions.TREAT_WATER);
        }
        return actions;
    }

    private boolean areTrainTracksBuildable(String lobbyCode) {
        IGame game = super.getGame(lobbyCode);
        return game.getTracksLeft() >= 0;
    }

    private boolean isHospitalBuildable() {
        //TODO: Implement logic in #85
        return true;
    }

    private boolean isKnowledgeShareable() {
        //TODO: Implement logic in #87
        return true;
    }

    private boolean isInfectionTreatable() {
        //TODO: Implement logic in #88
        return true;
    }

    private boolean isPlagueResearchable() {
        //TODO: Implement logic in #179
        return true;
    }

    private boolean isWaterTreatmentPlaceable() {
        //TODO: Implement logic in #84
        return true;
    }

    @Override
    public void movePlayer(IUser user, String lobbyId, ICity city, ICard card) throws GameManagementException {
        IGame game = super.getGame(lobbyId);

        IGameState gameState = game.getState();
        if (!(gameState instanceof PlayerTurnState)) {
            LOG.error("[LobbyID: {}] Game is not in a state that allows moving players", lobbyId);
            throw new GameManagementException("Game is not in a state that allows moving players");
        }

        IPlayer player = game.getCurrentPlayer();
        if (!player.getUser()
                   .equals(user)) {
            LOG.error("[LobbyID: {}] {} is not the current player", lobbyId, user.getUsername());
            throw new GameManagementException("Player is not the current player");
        }

        Map<ICity, List<ICard>> availableDestinations = connectionManagement.getAvailableDestinations(
                lobbyId,
                player.getCurrentPosition()
                      .getId()
        );
        boolean citiesConnectedByLand = availableDestinations.containsKey(city) && availableDestinations.get(city)
                                                                                                        .isEmpty();
        boolean citiesConnectedBySea = availableDestinations.containsKey(city) && !availableDestinations.get(city)
                                                                                                        .isEmpty();
        if (!citiesConnectedByLand && !citiesConnectedBySea) {
            LOG.error(
                    "[LobbyID: {}] Failed to move {}.There is no available connection between {} and {}",
                    lobbyId,
                    player.getUser()
                          .getUsername(),
                    player.getCurrentPosition()
                          .getName()
                          .getDisplayName(),
                    city.getName()
                        .getDisplayName()
            );
            throw new GameManagementException("There is no available connection between " + player.getCurrentPosition()
                                                                                                  .getName()
                                                                                                  .getDisplayName() + " " + "and " + city.getName()
                                                                                                                                         .getDisplayName());
        }

        if (citiesConnectedByLand) {
            LOG.debug(
                    "[LobbyID: {}] Moving {} to city {}",
                    lobbyId,
                    player.getUser()
                          .getUsername(),
                    city.getName()
                        .getDisplayName()
            );
            player.setCurrentPosition(city);
            ((PlayerTurnState) gameState).reduceActionsRemaining(game);
            return;
        }

        boolean playerIsSailor = player.getRole()
                                       .getName()
                                       .equals(RoleEnum.SAILOR);
        if (!playerIsSailor) {
            playerManagement.discardCard(game.getGameId(), player, card);
        }

        LOG.debug(
                "[LobbyID: {}] {} sails to {}",
                lobbyId,
                player.getUser()
                      .getUsername(),
                city.getName()
                    .getDisplayName()
        );
        player.setCurrentPosition(city);
        ((PlayerTurnState) gameState).reduceActionsRemaining(game);
    }

    @Override
    public void buildTrainTrack(IUser user, String lobbyId, IConnection connection) throws GameManagementException {
        IGame game = super.getGame(lobbyId);

        IGameState gameState = game.getState();
        if (!(gameState instanceof PlayerTurnState)) {
            LOG.error("[LobbyID: {}] Game is not in a state that allows to build train tracks", lobbyId);
            throw new GameManagementException("Game is not in a state that allows to build train tracks");
        }

        IPlayer player = game.getCurrentPlayer();
        if (!player.getUser()
                   .equals(user)) {
            LOG.error("[LobbyID: {}] {} is not the current player", lobbyId, user.getUsername());
            throw new GameManagementException("Player is not the current player");
        }

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
            throw new GameManagementException("Connection between " + connection.getCityNames()
                                                                                .get(0) + " and " + connection.getCityNames()
                                                                                                              .get(1) + " is not buildable");
        }

        game.getConnectionRepository()
            .getConnectionByID(connection.getId())
            .buildTrainTracks(true);
        game.setTracksLeft(game.getTracksLeft() - 1);
        ((PlayerTurnState) gameState).reduceActionsRemaining(game);
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

        if (game.getCurrentPlayer()
                .getRole()
                .getName() == RoleEnum.RAILWAY_PERSON) {
            if (game.getState() instanceof PlayerTurnState && !(game.getState() instanceof BuildExtraTrainTrackState)) {
                CityName cityName = connection.getCityNames()
                                              .stream()
                                              .filter(name -> !name.equals(player.getCurrentPosition()
                                                                                 .getName()))
                                              .findFirst()
                                              .orElseThrow(() -> new GameManagementException(
                                                      "Error while building train track"));

                game.setState(new BuildExtraTrainTrackState(connectionManagement.getBuildableTrainTracks(
                        lobbyId,
                        cityName.getId()
                )));
            } else {
                game.setState(game.getPreviousState());
            }
        }
    }

    /**
     * Retrieves a list of buildable train tracks for the current player in the specified game.
     * <p>
     * If the game is in the BuildExtraTrainTrackState, it returns the connections from that state.
     * Otherwise, it fetches the buildable train tracks based on the player's current position.
     *
     * @param lobbyId The ID of the lobby
     * @param game The game instance
     * @param player The current player
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
    }

    public void unlockGameInWaitForConfirmation(String lobbyId) {
        IGame game = getGame(lobbyId);
        game.setState(game.getPreviousState());
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
            playerTurnState.setActionsRemaining(playerTurnState.getActionsRemaining() + amount);
        }
    }
}
