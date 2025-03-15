package de.uol.swp.server.game.states;

import de.uol.swp.common.game.StateType;
import de.uol.swp.server.cards.data.eventcards.OnTheMoveDayAndNightEventCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventStateTest {
    private EventState state;

    @BeforeEach
    void setUp() {
        state = new EventState(new OnTheMoveDayAndNightEventCard(1));
    }

    @Test
    void testGetEventCard() {
        assertEquals(new OnTheMoveDayAndNightEventCard(1), state.getEventCard());
    }

    @Test
    void testStateType() {
        assertEquals(StateType.EVENT_STATE, state.getStateType());
    }
}
