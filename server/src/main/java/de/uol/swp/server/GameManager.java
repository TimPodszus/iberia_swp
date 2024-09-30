package de.uol.swp.server;

import de.uol.swp.common.game.Action;
import de.uol.swp.common.user.User;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.lobby.data.Lobby;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class GameManager {
    private Map<String, GameController> gameControllers = new ConcurrentHashMap<>();

    public void createGameForLobby(Lobby lobby) {
        String lobbyId = lobby.getLobbyCode();
        if (!gameControllers.containsKey(lobbyId)) {
            GameStore.getInstance()
                     .addGame(lobbyId, new Game());
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
