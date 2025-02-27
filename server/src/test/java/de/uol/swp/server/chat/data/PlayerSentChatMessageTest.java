package de.uol.swp.server.chat.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit tests for the PlayerChatMessage class.
 */
public class PlayerSentChatMessageTest {

    /**
     * Tests the constructor of PlayerChatMessage.
     * Verifies that the lobbyId, sender, and message are correctly set.
     */
    @Test
    void testConstructor() {
        PlayerChatMessage playerChatMessage = new PlayerChatMessage("lobbyId", "sender", "message");
        assertEquals("lobbyId", playerChatMessage.getLobbyId());
        assertEquals("sender", playerChatMessage.getSender());
        assertEquals("message", playerChatMessage.getMessage());
    }

    @Test
    void testEquals() {
        PlayerChatMessage playerChatMessage1 = new PlayerChatMessage("lobbyId", "sender", "message");
        PlayerChatMessage playerChatMessage2 = new PlayerChatMessage("lobbyId", "sender","message");
        assertEquals(playerChatMessage1, playerChatMessage2);
    }

    @Test
    void testEqualsWithSameObject() {
        PlayerChatMessage playerChatMessage = new PlayerChatMessage("lobbyId", "sender", "message");
        assertEquals(playerChatMessage, playerChatMessage);
    }

    @Test
    void testEqualsWithDifferentObject() {
        PlayerChatMessage playerChatMessage = new PlayerChatMessage("lobbyId", "sender", "message");
        Object object = new Object();
        assertNotEquals(playerChatMessage, object);
    }

    @Test
    void testHashCode() {
        PlayerChatMessage playerChatMessage1 = new PlayerChatMessage("lobbyId", "sender", "message");
        PlayerChatMessage playerChatMessage2 = new PlayerChatMessage("lobbyId", "sender","message");
        assertEquals(playerChatMessage1.hashCode(), playerChatMessage2.hashCode());
    }
}