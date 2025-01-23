package de.uol.swp.server.game.states;

import static org.junit.jupiter.api.Assertions.*;

import de.uol.swp.common.game.StateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WaitForPositioningTest {

    private WaitForPositioning state;

    @BeforeEach
    void setUp() {
        state = new WaitForPositioning();
    }

    @Test
    void testInitialStateType() {
        assertEquals(StateType.WAIT_FOR_POSITIONING_STATE, state.getStateType(), "The initial state type should be WAIT_FOR_POSITIONING_STATE");
    }

    @Test
    void testInitialPositionedPlayersCount() {
        assertEquals(0, state.getPositionedPlayersCount(), "Initially, no players should be positioned.");
    }

    @Test
    void testSetPositionedPlayersCount() {
        state.setPositionedPlayersCount(1);
        assertEquals(1, state.getPositionedPlayersCount(), "After setting, one player should be positioned.");
    }
}

