package de.uol.swp.common.lobby.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GetLobbyRequestTest {
    private final String lobbyId = "test";

    /**
     * Tests the creation of a GetLobbyRequest.
     * Verifies that the lobby code and user are correctly set.
     */
    @Test
    void createGetLobbyRequest() {
        GetLobbyRequest request = new GetLobbyRequest(lobbyId);
        assertEquals(lobbyId, request.getLobbyId());
    }
}
