package de.uol.swp.common.chat.messages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit tests for the ServerChatMessage class.
 */
public class ServerChatMessageTest {

    /**
     * Tests the constructor of ServerChatMessage.
     * Verifies that the lobbyId is correctly set.
     */
    @Test
    void testConstructor() {
        ServerChatMessage serverChatMessage = new ServerChatMessage("lobbyId", "message");
        assertEquals("lobbyId", serverChatMessage.getLobbyId());
    }

    /**
     * Tests the equals method of ServerChatMessage.
     * Verifies that two ServerChatMessage objects with the same values are equal.
     */
    @Test
    void testEquals() {
        ServerChatMessage serverChatMessage = new ServerChatMessage("lobbyId", "message");
        ServerChatMessage serverChatMessage2 = new ServerChatMessage("lobbyId", "message");
        assertEquals(serverChatMessage, serverChatMessage2);
    }

    /**
     * Tests the equals method of ServerChatMessage.
     * Verifies that a ServerChatMessage object is equal to itself.
     */
    @Test
    void testEquals_SameObject() {
        ServerChatMessage serverChatMessage = new ServerChatMessage("lobbyId", "message");
        assertEquals(serverChatMessage, serverChatMessage);
    }

    /**
     * Tests the equals method of ServerChatMessage.
     * Verifies that a ServerChatMessage object is not equal to an object of a different type.
     */
    @Test
    void testEquals_DifferentObject() {
        ServerChatMessage serverChatMessage = new ServerChatMessage("lobbyId", "message");
        Object object = new Object();
        assertNotEquals(serverChatMessage, object);
    }

    /**
     * Tests the hashCode method of ServerChatMessage.
     * Verifies that two ServerChatMessage objects with the same values have the same hash code.
     */
    @Test
    void testHashCode() {
        ServerChatMessage serverChatMessage = new ServerChatMessage("lobbyId", "message");
        ServerChatMessage serverChatMessage2 = new ServerChatMessage("lobbyId", "message");
        assertEquals(serverChatMessage.hashCode(), serverChatMessage2.hashCode());
    }
}