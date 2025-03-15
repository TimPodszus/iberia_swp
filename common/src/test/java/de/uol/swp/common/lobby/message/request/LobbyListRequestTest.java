package de.uol.swp.common.lobby.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for LobbyListRequest.
 */
class LobbyListRequestTest {

    /**
     * Tests the creation of a LobbyListRequest object.
     */
    @Test
    void createLobbyListRequest() {
        LobbyListRequest request = new LobbyListRequest();
        assertNotNull(request);
    }
}
