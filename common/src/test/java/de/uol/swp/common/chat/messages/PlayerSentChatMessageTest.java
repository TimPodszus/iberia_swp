package de.uol.swp.common.chat.messages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test class for PlayerChatMessage.
 */
public class PlayerSentChatMessageTest {

    /**
     * Tests the constructor of PlayerChatMessage.
     */
    @Test
    void testConstructor() {
        PlayerSentChatMessage playerSentChatMessage = new PlayerSentChatMessage("lobbyId", "sender", "message");
        assertEquals("lobbyId", playerSentChatMessage.getLobbyId());
        assertEquals("sender", playerSentChatMessage.getSender());
        assertEquals("message", playerSentChatMessage.getMessage());
    }

    /**
     * Tests the equals method of PlayerChatMessage for two equal objects.
     */
    @Test
    void testEquals() {
        PlayerSentChatMessage playerSentChatMessage = new PlayerSentChatMessage("lobbyId", "sender", "message");
        PlayerSentChatMessage playerSentChatMessage2 = new PlayerSentChatMessage("lobbyId", "sender", "message");
        assertEquals(playerSentChatMessage, playerSentChatMessage2);
    }

    /**
     * Tests the equals method of PlayerChatMessage for the same object.
     */
    @Test
    void testEquals_SameObject() {
        PlayerSentChatMessage playerSentChatMessage = new PlayerSentChatMessage("lobbyId", "sender", "message");
        assertEquals(playerSentChatMessage, playerSentChatMessage);
    }

    /**
     * Tests the equals method of PlayerChatMessage for different objects.
     */
    @Test
    void testEquals_DifferentObject() {
        PlayerSentChatMessage playerSentChatMessage = new PlayerSentChatMessage("lobbyId", "sender", "message");
        Object object = new Object();
        assertNotEquals(playerSentChatMessage, object);
    }

    /**
     * Tests the hashCode method of PlayerChatMessage.
     */
    @Test
    void testHashCode() {
        PlayerSentChatMessage playerSentChatMessage = new PlayerSentChatMessage("lobbyId", "sender", "message");
        PlayerSentChatMessage playerSentChatMessage2 = new PlayerSentChatMessage("lobbyId", "sender", "message");
        assertEquals(playerSentChatMessage.hashCode(), playerSentChatMessage2.hashCode());
    }
}