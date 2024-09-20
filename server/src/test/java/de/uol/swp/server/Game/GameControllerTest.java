package de.uol.swp.server.Game;

import de.uol.swp.common.user.User;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.game.GameController;
import de.uol.swp.server.lobby.Lobby;
import de.uol.swp.server.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameControllerTest {
    private GameController gameController;
    private Lobby lobby;
    private Board board;
    private List<Player> players;
    private Set<User> userSet;

    @BeforeEach
    public void setUp() {
        userSet = new HashSet<>();
        User user1 = mock(User.class);
        User user2 = mock(User.class);
        when(user1.getUsername()).thenReturn("User One");
        when(user2.getUsername()).thenReturn("User Two");
        userSet.add(user1);
        userSet.add(user2);
        lobby = mock(Lobby.class);
        when(lobby.getUsers()).thenReturn(userSet);

        board = mock(Board.class);
        players = new ArrayList<>();

        gameController = new GameController(lobby);
    }

    @Test
    public void testGameInitializationAndPlayerCreation() {
        assertEquals(
                2,
                gameController.getPlayers()
                              .size()
        );
        assertNotNull(gameController.getPlayers()
                                    .get(0)
                                    .getUser());
    }

    @Test
    public void testGameStartAndFirstTurn() throws InterruptedException {
        gameController.startGame();
        assertNotNull(gameController.getCurrentTurn());
        assertEquals(0, gameController.getCurrentPlayerIndex());
    }

    @Test
    public void testTurnSwitching() throws InterruptedException {
        gameController.startGame();
        gameController.finishTurn(gameController.getPlayers()
                                                .get(0));
        assertEquals(1, gameController.getCurrentPlayerIndex());
    }

    @Test
    public void testGameOverCondition() throws InterruptedException {
        when(gameController.isGameOver()).thenReturn(true);
        gameController.startGame();
        gameController.nextTurn();
        assertTrue(gameController.isGameOver());
    }

}
