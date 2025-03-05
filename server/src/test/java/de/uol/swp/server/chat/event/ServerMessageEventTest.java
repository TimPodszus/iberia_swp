package de.uol.swp.server.chat.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the ServerMessageEvent.
 */
public class ServerMessageEventTest {

    /**
     * Tests the constructor of ServerMessageEvent.
     * Verifies that the lobbyId and message are correctly set.
     */
    @Test
    void testConstructor() {
        ServerMessageEvent serverMessageEvent = new ServerMessageEvent("lobbyId", "message");
        assertEquals("lobbyId", serverMessageEvent.getLobbyId());
        assertEquals("message", serverMessageEvent.getMessage());
    }
}
