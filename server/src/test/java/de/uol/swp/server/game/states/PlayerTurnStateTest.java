package de.uol.swp.server.game.states;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.uol.swp.common.game.StateType;
import de.uol.swp.server.game.data.IGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerTurnStateTest {
    private PlayerTurnState state;
    private IGame mockGame;

    @BeforeEach
    void setUp() {
        state = new PlayerTurnState();
        mockGame = mock(IGame.class);
    }

    @Test
    void testInitialStateType() {
        assertEquals(
                StateType.PLAYER_TURN_STATE,
                state.getStateType(),
                "The initial state type should be PLAYER_TURN_STATE"
        );
    }

    @Test
    void testInitialActionsRemaining() {
        assertEquals(4, state.getActionsRemaining(), "Initially, there should be 4 actions remaining.");
    }

    @Test
    void testReduceActionsRemainingOnce() {
        state.reduceActionsRemaining(mockGame);
        assertEquals(3, state.getActionsRemaining(), "After one action, 3 actions should remain.");
        verify(mockGame, never()).setState(any());
    }

    @Test
    void testReduceActionsRemainingUntilZero() {
        for (int i = 0; i < 4; i++) {
            state.reduceActionsRemaining(mockGame);
        }
        assertEquals(0, state.getActionsRemaining(), "After four actions, no actions should remain.");
        verify(mockGame).setState(any(DrawCardState.class));
    }
}