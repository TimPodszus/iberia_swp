package de.uol.swp.common.lobby.message.response;

import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for GetLobbyResponse.
 */
class GetLobbyResponseTest {

    /**
     * A sample ILobbyDTO instance used for testing.
     */
    ILobbyDTO lobbyDTO = new LobbyDTO("testcode", "test", List.of(), null, 4);

    /**
     * Tests the GetLobbyResponse constructor and getLobbyDTO method.
     */
    @Test
    void testGetLobbyResponse() {
        GetLobbyResponse getLobbyResponse = new GetLobbyResponse(lobbyDTO);
        assertEquals(lobbyDTO, getLobbyResponse.getLobbyDTO());
    }
}
