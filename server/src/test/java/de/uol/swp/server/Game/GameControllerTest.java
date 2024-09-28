package de.uol.swp.server.Game;


import de.uol.swp.common.game.action.Action;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.lobby.Lobby;
import org.junit.jupiter.api.BeforeEach;
import de.uol.swp.common.user.User;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.server.player.Player;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    private GameController gameController;

    @Mock
    private Lobby mockLobby;

    @Mock
    private Board mockBoard;

    @Mock
    private Player mockPlayer;

    @Mock
    private User mockUser;

    @Mock
    private Action mockAction;

    @Mock
    private GameTurn mockGameTurn;

    @BeforeEach
    void setUp() {
        Set<User> users = new HashSet<>();
        users.add(mockUser);

        when(mockUser.getUsername()).thenReturn("TestUser");
        when(mockPlayer.getUser()).thenReturn(mockUser);
        List<Player> playerList = new ArrayList<>();
        playerList.add(mockPlayer);
        gameController.setPlayers(playerList);
    }
    /*
    !!!!!!!!!!!
    Tests stehen noch aus bis zur Überarbeitung des GameControllers nach StatePattern
    !!!!!!!!!!!
    @Test
    void testStartGame() throws InterruptedException {
        gameController.startGame();
        assertNotNull(gameController.getCurrentTurn(), "Der erste Zug sollte initialisiert werden.");
        verify(gameController, never()).isGameOver();
    }

    @Test
    void testNextTurn() throws InterruptedException {
        gameController.nextTurn();
        verify(mockPlayer, times(1)).getUser();
        assertNotNull(gameController.getCurrentTurn(), "Der aktuelle Zug sollte nicht null sein.");
    }

    @Test
    void testFinishTurn() throws InterruptedException {
        gameController.finishTurn(mockPlayer);
        verify(mockPlayer, times(1)).getUser();
        verify(gameController, never()).isGameOver();
    }

    @Test
    void testReceiveActionMessage_WhenCurrentPlayer() {
        when(mockPlayer.getUser()).thenReturn(mockUser);
        gameController.receiveActionMessage(mockUser, mockAction);
        verify(mockPlayer, times(1)).getUser();
        verify(gameController).processPlayerAction(mockAction);
    }

    @Test
    void testReceiveActionMessage_WhenNotCurrentPlayer() {
        User otherUser = mock(User.class);
        when(mockPlayer.getUser()).thenReturn(otherUser);
        gameController.receiveActionMessage(mockUser, mockAction);
        verify(mockPlayer, times(1)).getUser();
        verify(gameController, never()).processPlayerAction(mockAction);
    }

     */
}
