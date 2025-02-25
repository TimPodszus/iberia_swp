package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlaceExtraWaterTreatmentStateTest {

    @Test
    void testGetStateType() {
        PlaceExtraWaterTreatmentState state = new PlaceExtraWaterTreatmentState();
        assertEquals(StateType.PLACE_EXTRA_WATER_TREATMENT_STATE, state.getStateType());
    }
}