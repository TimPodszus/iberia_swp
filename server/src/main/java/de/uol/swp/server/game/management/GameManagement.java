package de.uol.swp.server.game.management;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.WaitForPositioning;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.Player;

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
    public Game createAndInitializeGame(CreateGameRequest request){
        Game game = new Game(request.getUsers(), request.getDifficulty());
        GameStore.getInstance().addGame(request.getLobbyCode(), game);
        return game;
    }

    /**
     * Sets the starting position of a user's player character in a game,
     * if the game is currently in a state that allows setting positioning.
     * Updates the game state if all players have been positioned.
     *
     * @param user The user whose position is to be set
     * @param lobbyCode The lobby code of the game
     * @param cityDTO The city to position the player at
     */
    public void setPositioning(User user, String lobbyCode, CityDTO cityDTO) {
        Game game = getGame(lobbyCode);
        if (game.getState() instanceof WaitForPositioning waitForPositioning) {
            List<Player> players = game.getPlayers();
            Player requestPlayer = null;
            for (Player player : players) {
                if (player.getUser().getUsername().equals(user.getUsername()) && player.getCurrentPosition() == null) {
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
            if (waitForPositioning.getPositionedPlayersCount() == game.getPlayers().size()) {
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
    private Game getGame(String lobbyCode) {
        return (Game) GameStore.getInstance().getGame(lobbyCode);
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
