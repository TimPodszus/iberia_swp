package de.uol.swp.common.game.message.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EndGameEventTest {

    /**
     * Tests the equals and hashCode methods of EndGameEvent.
     */
    @Test
    void testEqualsAndHashCode() {
        EndGameEvent event1 = new EndGameEvent("lobby123", true);
        EndGameEvent event2 = new EndGameEvent("lobby123", true);
        EndGameEvent event3 = new EndGameEvent("lobby123", false);
        EndGameEvent event4 = new EndGameEvent("lobby456", true);

        assertEquals(event1, event1);
        assertEquals(event1, event2);
        assertNotEquals(event1, event3);
        assertNotEquals(event1, event4);
        assertNotEquals(null, event1);
        assertNotEquals(event1, new Object());

        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1.hashCode(), event3.hashCode());
        assertNotEquals(event1.hashCode(), event4.hashCode());
    }
}
