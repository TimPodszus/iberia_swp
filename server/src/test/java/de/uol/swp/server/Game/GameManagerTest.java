package de.uol.swp.server.Game;

import de.uol.swp.common.enums.Action;
import de.uol.swp.common.enums.ActionType;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.server.GameManager;
import de.uol.swp.server.game.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    private GameManager gameManager;
    private GameController mockGameController;
    private Lobby mockLobby;
    private User mockUser;

    @BeforeEach
    void setUp() {
        gameManager = new GameManager();
        mockGameController = mock(GameController.class);
        mockLobby = mock(Lobby.class);
        mockUser = mock(User.class);

        when(mockLobby.getId()).thenReturn("lobby123");
    }

    @Test
    void createGameForLobby_CreatesNewGameIfNotExists() {
        gameManager.createGameForLobby(mockLobby);
        assertNotNull(gameManager.getGameController("lobby123"));
        verify(mockGameController, times(0)).initializeGame();
    }

    @Test
    void createGameForLobby_DoesNotCreateNewGameIfAlreadyExists() {
        gameManager.createGameForLobby(mockLobby);
        gameManager.createGameForLobby(mockLobby);
        assertEquals(1, gameManager.getGameControllers()
                                   .size());
    }

    @Test
    void endGame_RemovesGameController() {
        gameManager.createGameForLobby(mockLobby);
        gameManager.endGame("lobby123");
        assertNull(gameManager.getGameController("lobby123"));
    }

    @Test
    void receiveAndForwardActionMessage_ForwardsCorrectly() {
        gameManager.createGameForLobby(mockLobby);
        gameManager.receiveAndForwardActionMessage(mockUser, new Action(ActionType.MOVE), "lobby123");
        verify(mockGameController).receiveActionMessage(mockUser, new Action(ActionType.MOVE));
    }
}
