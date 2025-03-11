package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlacePreventionMarkerStateTest {

    @Test
    public void testGetStateType() {
        PlacePreventionMarkerState state = new PlacePreventionMarkerState();
        assertEquals(StateType.PLACE_PREVENTION_MARKER_STATE, state.getStateType());
    }
}