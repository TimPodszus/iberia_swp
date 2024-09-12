package de.uol.swp.server;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.server.game.GameController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    private Map<String, GameController> gameControllers = new ConcurrentHashMap<>();

    public void createGameForLobby(Lobby lobby) {
        String lobbyId = lobby.getId();
        if (!gameControllers.containsKey(lobbyId)) {
            GameController gameController = new GameController(lobby);
            gameControllers.put(lobbyId, gameController);
            gameController.initializeGame();
        }
    }

    public GameController getGameController(String lobbyId) {
        return gameControllers.get(lobbyId);
    }

    public void endGame(String lobbyId) {
        GameController gameController = gameControllers.remove(lobbyId);
    }
}
