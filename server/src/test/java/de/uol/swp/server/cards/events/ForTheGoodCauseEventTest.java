package de.uol.swp.server.cards.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test class for the ForTheGoodCauseEvent class.
 */
public class ForTheGoodCauseEventTest {

    /**
     * Tests the constructor of the ForTheGoodCauseEvent class.
     */
    @Test
    void testConstructor() {
        ForTheGoodCauseEvent forTheGoodCauseEvent = new ForTheGoodCauseEvent("LobbyID", "username");
        assertEquals("LobbyID", forTheGoodCauseEvent.getLobbyId());
        assertEquals("username", forTheGoodCauseEvent.getUsername());
    }

    /**
     * Tests the equals method of the ForTheGoodCauseEvent class.
     */
    @Test
    void testEquals() {
        ForTheGoodCauseEvent forTheGoodCauseEvent1 = new ForTheGoodCauseEvent("LobbyID", "username");
        ForTheGoodCauseEvent forTheGoodCauseEvent2 = new ForTheGoodCauseEvent("LobbyID", "username");
        assertEquals(forTheGoodCauseEvent1, forTheGoodCauseEvent2);
    }

    /**
     * Tests the equals method with the same object.
     */
    @Test
    void testEqualsWithSameObject() {
        ForTheGoodCauseEvent forTheGoodCauseEvent = new ForTheGoodCauseEvent("LobbyID", "username");
        assertEquals(forTheGoodCauseEvent, forTheGoodCauseEvent);
    }

    /**
     * Tests the equals method with a different type of object.
     */
    @Test
    void testEqualsWithWrongObject() {
        ForTheGoodCauseEvent forTheGoodCauseEvent = new ForTheGoodCauseEvent("LobbyID", "username");
        assertNotEquals(forTheGoodCauseEvent, new Object());
    }

    /**
     * Tests the hashCode method of the ForTheGoodCauseEvent class.
     */
    @Test
    void testHashCode() {
        ForTheGoodCauseEvent forTheGoodCauseEvent = new ForTheGoodCauseEvent("LobbyID", "username");
        assertEquals(forTheGoodCauseEvent.hashCode(), forTheGoodCauseEvent.hashCode());
    }
}
