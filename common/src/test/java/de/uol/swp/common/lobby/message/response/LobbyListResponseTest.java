package de.uol.swp.common.lobby.message.response;

import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for LobbyListResponse.
 */
class LobbyListResponseTest {
    /**
     * A list of ILobbyDTO objects representing the lobbies.
     */
    private final List<ILobbyDTO> lobbies = List.of(new LobbyDTO("testcode", "test", List.of(), null, 4));

    /**
     * Tests the creation of a LobbyListResponse and verifies that the lobbies are correctly set.
     */
    @Test
    void createLobbyListResponse() {
        LobbyListResponse message = new LobbyListResponse(lobbies);
        assertEquals(message.getLobbies(), lobbies);
    }
}
