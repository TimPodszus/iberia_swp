package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EndGameStateTest {
    private EndGameState state;

    @BeforeEach
    void setUp() {
        state = new EndGameState();
    }

    @Test
    void testStateType() {
        assertEquals(StateType.END_GAME_STATE, state.getStateType());
    }
}
