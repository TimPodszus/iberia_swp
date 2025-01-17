package de.uol.swp.common.game.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * This class contains unit tests for the AvailableActionsRequest class.
 */
public class AvailableActionsRequestTest {

    /**
     * Tests the constructor and getter method of AvailableActionsRequest.
     * Verifies that the lobby code is correctly set and retrieved.
     */
    @Test
    void testAvailableActionsRequest() {
        AvailableActionsRequest availableActionsRequest = new AvailableActionsRequest("LobbyCode");
        assertEquals("LobbyCode", availableActionsRequest.getLobbyCode());
    }

    /**
     * Tests the equals method of AvailableActionsRequest.
     * Verifies that two instances with the same lobby code are considered equal.
     */
    @Test
    void testEquals() {
        AvailableActionsRequest availableActionsRequest = new AvailableActionsRequest("LobbyCode");
        AvailableActionsRequest availableActionsRequest1 = new AvailableActionsRequest("LobbyCode");
        assertEquals(availableActionsRequest, availableActionsRequest1);
    }

    /**
     * Tests the equals method with the same object.
     * Verifies that an instance is equal to itself.
     */
    @Test
    void testEqualsWithEqualObjects() {
        AvailableActionsRequest availableActionsRequest = new AvailableActionsRequest("LobbyCode");
        assertEquals(availableActionsRequest, availableActionsRequest);
    }

    /**
     * Tests the equals method with different types of objects.
     * Verifies that an instance is not equal to an object of a different type.
     */
    @Test
    void testEqualsWithDifferentObjects() {
        AvailableActionsRequest availableActionsRequest = new AvailableActionsRequest("LobbyCode");
        Object object = new Object();
        assertNotEquals(availableActionsRequest, object);
    }

    /**
     * Tests the hashCode method of AvailableActionsRequest.
     * Verifies that the hash code of an instance is consistent.
     */
    @Test
    void testHashCode() {
        AvailableActionsRequest availableActionsRequest = new AvailableActionsRequest("LobbyCode");
        assertEquals(availableActionsRequest.hashCode(), availableActionsRequest.hashCode());
    }
}