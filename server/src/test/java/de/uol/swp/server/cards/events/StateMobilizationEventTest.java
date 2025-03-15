package de.uol.swp.server.cards.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test class for StateMobilizationEvent.
 */
public class StateMobilizationEventTest {

    /**
     * Tests the constructor of StateMobilizationEvent.
     */
    @Test
    void testConstructor() {
        StateMobilizationEvent stateMobilizationEvent = new StateMobilizationEvent("LobbyID");
        assertEquals("LobbyID", stateMobilizationEvent.getLobbyId());
    }

    /**
     * Tests the equals method of StateMobilizationEvent with two identical objects.
     */
    @Test
    void testEquals() {
        StateMobilizationEvent stateMobilizationEvent1 = new StateMobilizationEvent("LobbyID");
        StateMobilizationEvent stateMobilizationEvent2 = new StateMobilizationEvent("LobbyID");
        assertEquals(stateMobilizationEvent1, stateMobilizationEvent2);
    }

    /**
     * Tests the equals method of StateMobilizationEvent with the same object.
     */
    @Test
    void testEqualsWithSameObject() {
        StateMobilizationEvent stateMobilizationEvent = new StateMobilizationEvent("LobbyID");
        assertEquals(stateMobilizationEvent, stateMobilizationEvent);
    }

    /**
     * Tests the equals method of StateMobilizationEvent with a different object.
     */
    @Test
    void testEqualsWithDifferentObject() {
        StateMobilizationEvent stateMobilizationEvent = new StateMobilizationEvent("LobbyID");
        assertNotEquals(stateMobilizationEvent, new Object());
    }

    /**
     * Tests the hashCode method of StateMobilizationEvent.
     */
    @Test
    void testHashCode() {
        StateMobilizationEvent stateMobilizationEvent = new StateMobilizationEvent("LobbyID");
        assertEquals(stateMobilizationEvent.hashCode(), stateMobilizationEvent.hashCode());
    }
}
