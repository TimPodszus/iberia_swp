package de.uol.swp.common.lobby.message.request;

import de.uol.swp.common.lobby.message.request.LobbyJoinUserRequest;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the lobby join user request
 *
 * @see LobbyJoinUserRequest
 * @since 2023-05-14
 */
class LobbyJoinUserRequestTest {

    final String lobbyCode = "Test";
    final UserDTO user = new UserDTO("Marco", "Marco");


    /**
     * Test for creation of the LobbyJoinUserRequests
     * This test checks if the lobbyName and the user of the LobbyJoinUserRequest gets
     * set correctly during the creation of the request
     *
     * @since 2023-05-14
     */
    @Test
    void createLobbyJoinUserRequest() {
        LobbyJoinUserRequest request = new LobbyJoinUserRequest(lobbyCode, user);

        assertEquals(lobbyCode, request.getLobbyCode());
        assertEquals(user, request.getUser());
    }

}
