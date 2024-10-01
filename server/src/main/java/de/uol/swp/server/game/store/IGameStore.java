package de.uol.swp.server.game.store;

import de.uol.swp.server.game.data.IGame;

/**
 * Interface for storing and retrieving game instances.
 */
public interface IGameStore {

    /**
     * Retrieves a game instance by its lobby ID.
     *
     * @param lobbyId the ID of the lobby
     * @return the game instance associated with the given lobby ID
     */
    IGame getGame(String lobbyId);

    /**
     * Adds a game instance to the store.
     *
     * @param lobbyId the ID of the lobby
     * @param game    the game instance to add
     */
    void addGame(String lobbyId, IGame game);
}
