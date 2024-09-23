package de.uol.swp.server.Game;

import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.action.Action;
import de.uol.swp.common.game.action.ActionType;
import de.uol.swp.common.game.action.MoveAction;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityName;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.connection.ConnectionManagement;
import de.uol.swp.server.game.GameTurn;
import de.uol.swp.server.game.GameTurnException;
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
        player = new Player(null, null, null, new UserDTO("test", "test"));
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
    void testProcessActionDecrementsActions() throws GameTurnException {
        gameTurn.processAction(new Action(ActionType.MOVE));
        assertEquals(3, gameTurn.getActionsRemaining());
    }

    @Test
    void testProcessActionWithInvalidActionThrowsException() {
        Action action = new Action(null);
        assertThrows(IllegalArgumentException.class, () -> gameTurn.processAction(action));
    }

    @Test
    void testCheckTurnEndStartsDrawPhaseIfActionsZero() throws GameTurnException {
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

    /**
     * Tests the move action functionality.
     *
     * @throws GameTurnException if the action cannot be processed
     */
    @Test
    void testMoveAction() throws GameTurnException {
        ICityDTO cityDTO = CityRepository.getCityByName(CityName.PALMA_DE_MALLORCA)
                                         .toDto();
        ICityDTO destination = new ConnectionManagement().getAvailableDestinations(cityDTO)
                                                         .get(0);
        MoveAction moveAction = new MoveAction(destination);

        player.setCurrentPosition(City.fromDto(cityDTO));

        assertEquals(City.fromDto(cityDTO), player.getCurrentPosition());

        gameTurn.processAction(moveAction);

        assertEquals(City.fromDto(destination), player.getCurrentPosition());
    }
}
