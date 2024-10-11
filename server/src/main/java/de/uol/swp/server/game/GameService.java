package de.uol.swp.server.game;

import com.google.inject.Inject;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.response.BoardUpdateResponse;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagement;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Service responsible for managing game-related requests such as creating games.
 * It handles requests to create a game, initializing and validating the game setup,
 * and communicates the result back to the client through status responses.
 */
public class GameService extends AbstractService {
    GameManagement gameManagement = new GameManagement();
    /**
     * Constructs a new GameService and registers it with the specified EventBus.
     *
     * @param bus the EventBus to which the service will subscribe and post events
     */
    @Inject
    public GameService(EventBus bus) {
        super(bus);
    }

    /**
     * Handles incoming requests to create a game. This method initializes a game
     * through the GameManagement class, checks if the game creation was successful,
     * and sends an appropriate status response to the requester.
     *
     * @param request the game creation request containing necessary game initialization parameters
     */
    @Subscribe
    public void onCreateGameRequest(CreateGameRequest request) {
        boolean success = false;
        IGame game = gameManagement.createAndInitializeGame(request);
        if (game != null) {
            success = true;
        }
        sendStatusRespond(success);
        post(new BoardUpdateResponse(success,"Game Aktualisierung", GameMapper.toDTO((Game) game)));
    }

    /**
     * Placeholder for sending a positioning request to initialize player positions in the game.
     * This method needs to be implemented to handle the game setup and positioning of players.
     */
    public void sendPositioningRequest() {
        // TODO: Implement method to handle player positioning initialization
    }

    /**
     * Sends a status response indicating the success or failure of an operation.
     * This method creates a status response based on the success parameter and posts it to the EventBus.
     *
     * @param success a boolean indicating whether the operation was successful
     */
    private void sendStatusRespond(boolean success) {
        StatusResponse statusResponse;
        if (success) {
            statusResponse = new StatusResponse(true, "Game wurde erfolgreich erstellt!");
        } else {
            statusResponse = new StatusResponse(false, "Game konnte nicht erstellt werden!");
        }
        post(statusResponse);
    }
}
