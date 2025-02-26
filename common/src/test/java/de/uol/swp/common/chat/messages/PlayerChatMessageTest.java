package de.uol.swp.common.chat.messages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test class for PlayerChatMessage.
 */
public class PlayerChatMessageTest {

    /**
     * Tests the constructor of PlayerChatMessage.
     */
    @Test
    void testConstructor() {
        PlayerChatMessage playerChatMessage = new PlayerChatMessage("lobbyId", "sender", "message");
        assertEquals("lobbyId", playerChatMessage.getLobbyId());
        assertEquals("sender", playerChatMessage.getSender());
        assertEquals("message", playerChatMessage.getMessage());
    }

    /**
     * Tests the equals method of PlayerChatMessage for two equal objects.
     */
    @Test
    void testEquals() {
        PlayerChatMessage playerChatMessage = new PlayerChatMessage("lobbyId", "sender", "message");
        PlayerChatMessage playerChatMessage2 = new PlayerChatMessage("lobbyId", "sender", "message");
        assertEquals(playerChatMessage, playerChatMessage2);
    }

    /**
     * Tests the equals method of PlayerChatMessage for the same object.
     */
    @Test
    void testEquals_SameObject() {
        PlayerChatMessage playerChatMessage = new PlayerChatMessage("lobbyId", "sender", "message");
        assertEquals(playerChatMessage, playerChatMessage);
    }

    /**
     * Tests the equals method of PlayerChatMessage for different objects.
     */
    @Test
    void testEquals_DifferentObject() {
        PlayerChatMessage playerChatMessage = new PlayerChatMessage("lobbyId", "sender", "message");
        Object object = new Object();
        assertNotEquals(playerChatMessage, object);
    }

    /**
     * Tests the hashCode method of PlayerChatMessage.
     */
    @Test
    void testHashCode() {
        PlayerChatMessage playerChatMessage = new PlayerChatMessage("lobbyId", "sender", "message");
        PlayerChatMessage playerChatMessage2 = new PlayerChatMessage("lobbyId", "sender", "message");
        assertEquals(playerChatMessage.hashCode(), playerChatMessage2.hashCode());
    }
}