package de.uol.swp.common.chat.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit tests for the SendChatRequest class.
 */
public class SendChatRequestTest {

    /**
     * Tests the constructor of SendChatRequest.
     * Verifies that the lobbyId is correctly set.
     */
    @Test
    void testConstructor() {
        SendChatRequest sendChatRequest = new SendChatRequest("lobbyId", "message");
        assertEquals("lobbyId", sendChatRequest.getLobbyId());
    }

    /**
     * Tests the equals method of SendChatRequest.
     * Verifies that two SendChatRequest objects with the same lobbyId and message are equal.
     */
    @Test
    void testEquals() {
        SendChatRequest sendChatRequest = new SendChatRequest("lobbyId", "message");
        SendChatRequest sendChatRequest2 = new SendChatRequest("lobbyId", "message");
        assertEquals(sendChatRequest, sendChatRequest2);
    }

    /**
     * Tests the equals method of SendChatRequest.
     * Verifies that a SendChatRequest object is equal to itself.
     */
    @Test
    void testEquals_SameObject() {
        SendChatRequest sendChatRequest = new SendChatRequest("lobbyId", "message");
        assertEquals(sendChatRequest, sendChatRequest);
    }

    /**
     * Tests the equals method of SendChatRequest.
     * Verifies that a SendChatRequest object is not equal to an object of a different type.
     */
    @Test
    void testEquals_DifferentObject() {
        SendChatRequest sendChatRequest = new SendChatRequest("lobbyId", "message");
        Object object = new Object();
        assertNotEquals(sendChatRequest, object);
    }

    /**
     * Tests the hashCode method of SendChatRequest.
     * Verifies that two SendChatRequest objects with the same lobbyId and message have the same hash code.
     */
    @Test
    void testHashCode() {
        SendChatRequest sendChatRequest = new SendChatRequest("lobbyId", "message");
        SendChatRequest sendChatRequest2 = new SendChatRequest("lobbyId", "message");
        assertEquals(sendChatRequest.hashCode(), sendChatRequest2.hashCode());
    }
}