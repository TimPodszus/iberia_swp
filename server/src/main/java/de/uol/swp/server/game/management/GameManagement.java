package de.uol.swp.server.game.management;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.game.GameActions;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.WaitForPositioning;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.player.management.PlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.role.Role;
import de.uol.swp.server.role.RoleRepository;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.UserMapper;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages game related operations such as creating games,
 * setting player positions, and handling card draws.
 */
public class GameManagement implements IGameManagement {

    @Inject
    private IPlayerManagement playerManagement;

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
    void initializing(IGame game, List<IUser> users) throws PlayerManagementException {
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
            int cardsToDraw = switch (game.getPlayers()
                                          .size()) {
                case 2 -> 4;
                case 3 -> 3;
                default -> 2;
            };
            for (int i = 0; i < cardsToDraw; i++) {
                playerManagement.drawPlayerCard(game, player);
            }
            game.setCurrentPlayerIndex(game.getCurrentPlayerIndex() + 1);
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
        Player startingPlayer = null;
        for (Player player : game.getPlayers()) {
            for (Card card : player.getCards()) {
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

    void initiateInfections(IGame game) {
        int infectionAmount = 3;
        for (int i = 1; i <= 9; i++) {
            game.getCityManagement()
                .infectCityWithOwnPlague(game, drawInfectionCard(game), infectionAmount);
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
        IGame game = getGame(request.getLobbyCode());
        if (game.getState() instanceof WaitForPositioning waitForPositioning) {
            List<Player> players = game.getPlayers();
            Player requestPlayer = null;
            for (Player player : players) {
                if (player.getUser()
                          .getUsername()
                          .equals(request.getSession()
                                         .get()
                                         .getUser()
                                         .getUsername())) {
                    requestPlayer = player;
                    break;
                }
            }
            try {
                assert requestPlayer != null;
                requestPlayer.setStartingPosition(game.getCityRepository()
                                                      .getCityNameById(request.getCityId()));
                waitForPositioning.setPositionedPlayersCount(waitForPositioning.getPositionedPlayersCount() + 1);
            } catch (Exception e) {
                throw new GameManagementException("Failed to set Position");
            }
            if (waitForPositioning.getPositionedPlayersCount() == game.getPlayers()
                                                                      .size()) {
                game.setState(new PlayerTurnState());
                game.setCurrentPlayerIndex(0);
            }
        }
        return game;
    }

    /**
     * Retrieves a game based on the lobby code.
     *
     * @param lobbyCode The code of the lobby to retrieve the game from
     * @return The game associated with the given lobby code
     */
    private IGame getGame(String lobbyCode) {
        return GameStore.getInstance()
                        .getGame(lobbyCode);
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
        if (areTrainTracksBuildable()) {
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

    private boolean areTrainTracksBuildable() {
        //TODO: Implement logic in #86
        return true;
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
}
