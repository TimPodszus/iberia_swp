package de.uol.swp.server;

import de.uol.swp.server.chat.event.ServerMessageEvent;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameNotFoundException;
import de.uol.swp.server.game.store.GameStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

/**
 * Abstract base class for management-related functionalities.
 */
public abstract class AbstractManagement {
    private static final Logger LOG = LogManager.getLogger(AbstractManagement.class);

    /**
     * Retrieves the game instance associated with the given lobby ID.
     *
     * @param lobbyId the ID of the lobby
     * @return the game instance associated with the given lobby ID
     */
    public IGame getGame(String lobbyId) {
        IGame game = GameStore.getInstance()
                              .getGame(lobbyId);
        if (game == null) {
            LOG.error("[LobbyId: {}] Game not found.", lobbyId);
            throw new GameNotFoundException("Game with ID " + lobbyId + " not found.");
        }
        return game;
    }

    /**
     * Posts a message on the EventBus.
     *
     * @param lobbyId the ID of the lobby
     * @param message the message to send
     */
    protected void sendServerMessageEvent(String lobbyId, String message) {
        ServerMessageEvent serverMessage = new ServerMessageEvent(lobbyId, message);
        EventBus.getDefault()
                .post(serverMessage);
    }
}
