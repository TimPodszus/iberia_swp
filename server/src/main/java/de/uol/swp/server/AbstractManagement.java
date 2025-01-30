package de.uol.swp.server;

import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.store.GameStore;

/**
 * Abstract base class for management-related functionalities.
 */
public abstract class AbstractManagement {
    /**
     * Retrieves the game instance associated with the given lobby ID.
     *
     * @param lobbyId the ID of the lobby
     * @return the game instance associated with the given lobby ID
     */
    public IGame getGame(String lobbyId) {
        return GameStore.getInstance()
                        .getGame(lobbyId);
    }
}
