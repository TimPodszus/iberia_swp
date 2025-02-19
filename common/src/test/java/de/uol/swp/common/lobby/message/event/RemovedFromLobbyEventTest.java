package de.uol.swp.common.lobby.message.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the RemovedFromLobbyEvent.
 */
public class RemovedFromLobbyEventTest {

    /**
     * Tests the constructor of RemovedFromLobbyEvent.
     */
    @Test
    void testConstructor() {
        RemovedFromLobbyEvent removedFromLobbyEvent = new RemovedFromLobbyEvent("lobbyId");

        assertEquals("lobbyId", removedFromLobbyEvent.getLobbyId());
    }
}
