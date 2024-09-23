package de.uol.swp.server.Game;

import de.uol.swp.common.enums.Action;
import de.uol.swp.common.enums.ActionType;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.server.GameManager;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.server.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    private GameManager gameManager;
    private GameController mockGameController;
    private Lobby mockLobby;
    private User mockUser;
    private Player mockPlayer;
    private Action mockAction;
    private GameTurn mockCurrenturn;

    @BeforeEach
    void setUp() {
        gameManager = new GameManager();
        mockGameController = spy(new GameController(mock(Lobby.class)));
        mockLobby = mock(Lobby.class);
        mockUser = mock(User.class);
        mockPlayer = mock(Player.class);
        mockAction = mock(Action.class);
        mockCurrenturn = mock(GameTurn.class);

        when(mockLobby.getId()).thenReturn("lobby123");

        List<Player> playerList = new ArrayList<>();
        playerList.add(mockPlayer);
        mockGameController.setPlayers(playerList);
        mockGameController.setCurrentTurn(mockCurrenturn);

        when(mockPlayer.getUser()).thenReturn(mockUser);
        mockGameController.setCurrentPlayerIndex(0);
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
    void receiveActionMessage_UserIsCurrentPlayer_ProcessAction() {
        when(mockPlayer.getUser()).thenReturn(mockUser);

        mockGameController.receiveActionMessage(mockUser, mockAction);

        verify(mockGameController).processPlayerAction(mockAction);
    }

    @Test
    void receiveActionMessage_UserIsNotCurrentPlayer_DoNotProcessAction() {
        User otherUser = mock(User.class);
        when(mockPlayer.getUser()).thenReturn(otherUser);

        mockGameController.receiveActionMessage(mockUser, mockAction);

        verify(mockGameController, never()).processPlayerAction(mockAction);
    }
}
