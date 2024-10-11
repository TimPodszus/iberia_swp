package de.uol.swp.common.lobby.message.request;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GetLobbyRequestTest {
    private final String lobbyCode = "test";
    private final User userDto = new UserDTO("Test1", "qweasd");

    /**
     * Tests the creation of a GetLobbyRequest.
     * Verifies that the lobby code and user are correctly set.
     */
    @Test
    void createGetLobbyRequest() {
        GetLobbyRequest request = new GetLobbyRequest(lobbyCode, userDto);
        assertEquals(lobbyCode, request.getLobbyCode());
        assertEquals(userDto, request.getUser());
    }
}
