package de.uol.swp.common.lobby.message.response;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LobbyCreatedResponse.
 */
class LobbyCreatedResponseTest {
    final LobbyDTO lobby = new LobbyDTO(
            "test",
            "test",
            new ArrayList<>(),
            new UserDTO("test", "test"),
            1
    );

    /**
     * Tests the creation of a LobbyCreatedResponse.
     * <p>
     * This test verifies that the LobbyCreatedResponse is correctly created
     * and that the lobby data transfer object (DTO) is correctly set.
     */
    @Test
    void createLobbyCreatedResponse() {
        LobbyCreatedResponse response = new LobbyCreatedResponse(lobby);
        assertEquals(lobby, response.getLobbyDTO());
    }
}