package de.uol.swp.server.chat.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test class for the ServerChatMessage class.
 */
public class ServerChatMessageTest {

    /**
     * Tests the constructor of the ServerChatMessage class.
     * Verifies that the lobbyId and message are correctly set.
     */
    @Test
    void testConstructor() {
        IChatMessage serverChatMessage = new ServerChatMessage("lobbyId", "message");
        assertEquals("lobbyId", serverChatMessage.getLobbyId());
        assertEquals("message", serverChatMessage.getMessage());
    }

    @Test
    void testEquals() {
        IChatMessage serverChatMessage1 = new ServerChatMessage("lobbyId", "message");
        IChatMessage serverChatMessage2 = new ServerChatMessage("lobbyId", "message");
        assertEquals(serverChatMessage1, serverChatMessage2);
    }

    @Test
    void testEqualsWithSameObject() {
        IChatMessage serverChatMessage = new ServerChatMessage("lobbyId", "message");
        assertEquals(serverChatMessage, serverChatMessage);
    }

    @Test
    void testEqualsWithDifferentObject() {
        IChatMessage serverChatMessage = new ServerChatMessage("lobbyId", "message");
        Object object = new Object();
        assertNotEquals(serverChatMessage, object);
    }

    @Test
    void testHashCode() {
        IChatMessage serverChatMessage1 = new ServerChatMessage("lobbyId", "message");
        IChatMessage serverChatMessage2 = new ServerChatMessage("lobbyId", "message");
        assertEquals(serverChatMessage1.hashCode(), serverChatMessage2.hashCode());
    }
}
