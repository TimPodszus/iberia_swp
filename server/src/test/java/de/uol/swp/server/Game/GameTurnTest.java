package de.uol.swp.server.Game;

import de.uol.swp.common.game.ActionType;
import de.uol.swp.common.game.action.Action;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.server.game.GameTurnException;
import de.uol.swp.server.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameTurnTest {
    private GameTurn gameTurn;
    private Player player;
    private Board board;

    @BeforeEach
    void setUp() {
        player = new Player(null, null, null, new UserDTO("test", "test"));
        board = mock(Board.class);
        User mockUser = mock(User.class);
        gameTurn = new GameTurn(player, board);
        when(player.getUser()).thenReturn(mockUser);
    }

    @Test
    void testConstructorInitializesValuesCorrectly() {
        assertEquals(player, gameTurn.getCurrentPlayer());
        assertEquals(board, gameTurn.getBoard());
        assertEquals(4, gameTurn.getActionsRemaining());
        assertFalse(gameTurn.isDrawPhase());
        assertFalse(gameTurn.isInfectionPhase());
        assertFalse(gameTurn.isTurnOver());
    }

    @Test
    void testProcessActionDecrementsActions() throws GameTurnException {
        gameTurn.processAction(new Action(ActionType.MOVE));
        assertEquals(3, gameTurn.getActionsRemaining());
    }

    @Test
    void testCheckTurnEndStartsDrawPhaseIfActionsZero() throws GameTurnException {
        gameTurn.processAction(new Action(ActionType.MOVE));
        gameTurn.processAction(new Action(ActionType.MOVE));
        gameTurn.processAction(new Action(ActionType.MOVE));
        gameTurn.processAction(new Action(ActionType.MOVE));
        assertTrue(gameTurn.isDrawPhase());
    }

    @Test
    void testEndTurnSetsTurnOver() {
        gameTurn.endTurn();
        assertTrue(gameTurn.isTurnOver());
    }

    /**
     * Tests the move action functionality.
     */
    @Test
    void testMoveAction() {
        //TODO Implement test
    }
}
