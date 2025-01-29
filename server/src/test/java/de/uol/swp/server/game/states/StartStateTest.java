package de.uol.swp.server.game.states;

import static org.junit.jupiter.api.Assertions.*;

import de.uol.swp.common.game.StateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StartStateTest {
    private StartState state;

    @BeforeEach
    void setUp() {
        state = new StartState();
    }

    @Test
    void testStateType() {
        assertEquals(StateType.START_STATE, state.getStateType());
    }
}
