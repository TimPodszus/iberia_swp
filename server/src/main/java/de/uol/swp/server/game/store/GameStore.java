package de.uol.swp.server.game.store;

import de.uol.swp.server.game.data.IGame;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton class that stores and manages game instances.
 */
public class GameStore implements IGameStore {

    /**
     * The singleton instance of the GameStore.
     */
    private static GameStore instance;

    /**
     * The map of game instances, indexed by lobby ID.
     */
    private final Map<String, IGame> games = new ConcurrentHashMap<>();

    /**
     * Returns the singleton instance of the GameStore.
     *
     * @return the singleton instance of the GameStore
     */
    public static GameStore getInstance() {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    /**
     * Creates and returns a new instance of the GameStore.
     * This method is synchronized to ensure thread safety.
     *
     * @return a new instance of the GameStore
     */
    private static synchronized GameStore createInstance() {
        if (instance == null) {
            instance = new GameStore();
        }
        return instance;
    }

    @Override
    public IGame getGame(String lobbyId) {
        return games.get(lobbyId);
    }

    @Override
    public void addGame(String lobbyId, IGame game) {
        games.put(lobbyId, game);
    }
}
