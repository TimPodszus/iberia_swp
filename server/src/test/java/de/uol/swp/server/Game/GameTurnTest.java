package de.uol.swp.server.Game;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import de.uol.swp.server.game.GameTurn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import de.uol.swp.common.enums.Action;
import de.uol.swp.common.enums.ActionType;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.player.Player;

public class GameTurnTest {
    private GameTurn gameTurn;
    private Player player;
    private Board board;

    @BeforeEach
    public void setUp() {
        player = mock(Player.class);
        board = mock(Board.class);
        gameTurn = new GameTurn(player, board);
    }

    @Test
    public void testConstructorInitializesValuesCorrectly() {
        assertEquals(player, gameTurn.getCurrentPlayer());
        assertEquals(board, gameTurn.getBoard());
        assertEquals(4, gameTurn.getActionsRemaining());
        assertFalse(gameTurn.isDrawPhase());
        assertFalse(gameTurn.isInfectionPhase());
        assertFalse(gameTurn.isTurnOver());
    }

    @Test
    public void testProcessActionDecrementsActions() {
        gameTurn.processAction(new Action(ActionType.MOVE));
        assertEquals(3, gameTurn.getActionsRemaining());
    }

    @Test
    public void testProcessActionWithInvalidActionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> gameTurn.processAction(new Action(null)));
    }

    @Test
    public void testCheckTurnEndStartsDrawPhaseIfActionsZero() {
        gameTurn.processAction(new Action(ActionType.MOVE));
        gameTurn.processAction(new Action(ActionType.MOVE));
        gameTurn.processAction(new Action(ActionType.MOVE));
        gameTurn.processAction(new Action(ActionType.MOVE));
        assertTrue(gameTurn.isDrawPhase());
        assertFalse(gameTurn.isTurnOver());
    }

    @Test
    public void testEndTurnSetsTurnOver() {
        gameTurn.endTurn();
        assertTrue(gameTurn.isTurnOver());
    }
}
