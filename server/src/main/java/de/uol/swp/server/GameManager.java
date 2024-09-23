package de.uol.swp.server;

import de.uol.swp.common.game.action.Action;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.server.game.GameController;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
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

    public void receiveAndForwardActionMessage(User user, Action action, String lobbyId) {
        GameController gameController = getGameController(lobbyId);
        if (gameController != null) {
            gameController.receiveActionMessage(user, action);
        }
    }
}
