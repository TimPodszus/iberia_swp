package de.uol.swp.common.lobby.message.request;

import de.uol.swp.common.lobby.dto.ILobbyDTO;
import de.uol.swp.common.lobby.dto.LobbyDTO;

import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for UpdateLobbyRequest.
 */
class UpdateLobbyRequestTest {
    /**
     * The owner of the lobby.
     */
    IUserDTO owner = new UserDTO("Test1", "qweasd");

    /**
     * The lobby data transfer object.
     */
    ILobbyDTO lobbyDTO = new LobbyDTO("testcode", "test", List.of(owner), owner, 4);

    /**
     * Tests the UpdateLobbyRequest constructor and its getters.
     */
    @Test
    void testUpdateLobbyRequest() {
        UpdateLobbyRequest updateLobbyRequest = new UpdateLobbyRequest(lobbyDTO);
        assertEquals("testcode", updateLobbyRequest.getLobbyId());
        assertEquals(lobbyDTO, updateLobbyRequest.getLobbyDTO());
    }
}
