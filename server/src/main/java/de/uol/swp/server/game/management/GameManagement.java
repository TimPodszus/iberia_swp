package de.uol.swp.server.game.management;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.WaitForPositioning;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.role.Role;
import de.uol.swp.server.role.RoleRepository;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.UserMapper;

import java.util.Collections;
import java.util.List;

/**
 * Manages game related operations such as creating games,
 * setting player positions, and handling card draws.
 */
public class GameManagement implements IGameManagement {
    /**
     * Constructs a new GameManagement object.
     */
    public GameManagement() {
        //Todo: SpielInitialisierung
    }

    /**
     * Creates and initializes a game based on the provided creation request.
     * It sets up the game with specified users and difficulty level, and registers it in the game store.
     *
     * @param request The request containing the necessary data to create the game
     * @return The newly created game
     */
    public IGame createAndInitializeGame(CreateGameRequest request) {
        IGame game = new Game(request.getDifficulty(), request.getLobbyCode());
        GameStore.getInstance().addGame(request.getLobbyCode(), game);
        initializing(game, UserMapper.toUser(request.getUsers()));
        return game;
    }

    /**
     * Initializes the game by setting up players, assigning roles,
     * determining the starting player, and initiating city infections.
     *
     * @param game  The game instance to initialize
     * @param users The list of users participating in the game
     */
    void initializing(IGame game, List<IUser> users) {
        createPlayers(users, game);
        assignRoles(game);
        setStartingPlayer(game);
        initiateInfections(game);
    }

    /**
     * Creates player instances for the provided users, adds them to the game,
     * and assigns each player a specific number of starting cards based on the number of players.
     *
     * @param users The list of users to create players for
     * @param game  The game instance to add players to
     */
    private void createPlayers(List<IUser> users, IGame game) {
        for (IUser user : users) {
            Player player = new Player(user);
            game.getPlayers().add(player);
        }
        for (Player player : game.getPlayers()) {
            int cardsToDraw = switch ( game.getPlayers().size()) {
                case 2 -> 4;
                case 3 -> 3;
                default -> 2;
            };
            for (int i = 0; i < cardsToDraw; i++) {
                //TODO draw Card from Pile
                player.addCard(game.getPlayerCardDrawPile().get((int) Math.floor(Math.random() * game.getPlayerCardDrawPile().size())));
            }
            //TODO set position like in the real game
            player.setCurrentPosition(game.getCityRepository().getCities()
                                          .get((int) Math.floor(Math.random() * 48)));
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
                .infectCity(drawInfectionCard(), infectionAmount);
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
     * @param user      The user whose position is to be set
     * @param lobbyCode The lobby code of the game
     * @param cityDTO   The city to position the player at
     */
    public void setPositioning(IUser user, String lobbyCode, CityDTO cityDTO) {
        IGame game = getGame(lobbyCode);
        if (game.getState() instanceof WaitForPositioning waitForPositioning) {
            List<Player> players = game.getPlayers();
            Player requestPlayer = null;
            for (Player player : players) {
                if (player.getUser()
                          .getUsername()
                          .equals(user.getUsername()) && player.getCurrentPosition() == null) {
                    requestPlayer = player;
                    break;
                }
            }
            try {
                assert requestPlayer != null;
                requestPlayer.setStartingPosition(cityDTO.getName());
                waitForPositioning.setPositionedPlayersCount(waitForPositioning.getPositionedPlayersCount() + 1);
            } catch (Exception e) {
                // StatusResponse
            }
            if (waitForPositioning.getPositionedPlayersCount() == game.getPlayers()
                                                                      .size()) {
                game.setState(new PlayerTurnState());
            }
        }
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
     * Draws a player card from the deck.
     * This method needs to be implemented to define how player cards are drawn.
     */
    public void drawPlayerCard() {
        // TODO Implement Method
    }

    /**
     * Draws an infection card from the deck.
     *
     * @return The drawn infection card, or null if no card can be drawn
     */
    public InfectionCard drawInfectionCard() {
        return null; // This method needs proper implementation
    }

}
