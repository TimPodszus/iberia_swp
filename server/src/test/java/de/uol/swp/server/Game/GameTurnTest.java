package de.uol.swp.server.Game;

import de.uol.swp.common.game.action.Action;
import de.uol.swp.common.game.action.ActionType;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.server.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameTurnTest {
    private GameTurn gameTurn;
    private Player player;
    private Board board;

    @BeforeEach
    void setUp() {
        player = mock(Player.class);
        board = mock(Board.class);
        gameTurn = new GameTurn(player, board);
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
    void testProcessActionDecrementsActions() {
        gameTurn.processAction(new Action(ActionType.MOVE));
        assertEquals(3, gameTurn.getActionsRemaining());
    }

    @Test
    void testProcessActionWithInvalidActionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> gameTurn.processAction(new Action(null)));
    }

    @Test
    void testCheckTurnEndStartsDrawPhaseIfActionsZero() {
        gameTurn.processAction(new Action(ActionType.MOVE));
        gameTurn.processAction(new Action(ActionType.MOVE));
        gameTurn.processAction(new Action(ActionType.MOVE));
        gameTurn.processAction(new Action(ActionType.MOVE));
        assertTrue(gameTurn.isDrawPhase());
        assertFalse(gameTurn.isTurnOver());
    }

    @Test
    void testEndTurnSetsTurnOver() {
        gameTurn.endTurn();
        assertTrue(gameTurn.isTurnOver());
    }
}
