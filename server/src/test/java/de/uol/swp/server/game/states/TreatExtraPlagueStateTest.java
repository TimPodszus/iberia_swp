package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TreatExtraPlagueStateTest {
    @Test
    void testGetStateType() {
        TreatExtraPlagueState state = new TreatExtraPlagueState(4);
        assertEquals(StateType.TREAT_EXTRA_PLAGUE_STATE, state.getStateType());
    }
}
