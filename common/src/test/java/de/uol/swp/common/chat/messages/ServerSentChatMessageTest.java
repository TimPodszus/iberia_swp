package de.uol.swp.common.chat.messages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit tests for the ServerChatMessage class.
 */
public class ServerSentChatMessageTest {

    /**
     * Tests the constructor of ServerChatMessage.
     * Verifies that the lobbyId is correctly set.
     */
    @Test
    void testConstructor() {
        ServerSentChatMessage serverSentChatMessage = new ServerSentChatMessage("lobbyId", "message");
        assertEquals("lobbyId", serverSentChatMessage.getLobbyId());
    }

    /**
     * Tests the equals method of ServerChatMessage.
     * Verifies that two ServerChatMessage objects with the same values are equal.
     */
    @Test
    void testEquals() {
        ServerSentChatMessage serverSentChatMessage = new ServerSentChatMessage("lobbyId", "message");
        ServerSentChatMessage serverSentChatMessage2 = new ServerSentChatMessage("lobbyId", "message");
        assertEquals(serverSentChatMessage, serverSentChatMessage2);
    }

    /**
     * Tests the equals method of ServerChatMessage.
     * Verifies that a ServerChatMessage object is equal to itself.
     */
    @Test
    void testEquals_SameObject() {
        ServerSentChatMessage serverSentChatMessage = new ServerSentChatMessage("lobbyId", "message");
        assertEquals(serverSentChatMessage, serverSentChatMessage);
    }

    /**
     * Tests the equals method of ServerChatMessage.
     * Verifies that a ServerChatMessage object is not equal to an object of a different type.
     */
    @Test
    void testEquals_DifferentObject() {
        ServerSentChatMessage serverSentChatMessage = new ServerSentChatMessage("lobbyId", "message");
        Object object = new Object();
        assertNotEquals(serverSentChatMessage, object);
    }

    /**
     * Tests the hashCode method of ServerChatMessage.
     * Verifies that two ServerChatMessage objects with the same values have the same hash code.
     */
    @Test
    void testHashCode() {
        ServerSentChatMessage serverSentChatMessage = new ServerSentChatMessage("lobbyId", "message");
        ServerSentChatMessage serverSentChatMessage2 = new ServerSentChatMessage("lobbyId", "message");
        assertEquals(serverSentChatMessage.hashCode(), serverSentChatMessage2.hashCode());
    }
}