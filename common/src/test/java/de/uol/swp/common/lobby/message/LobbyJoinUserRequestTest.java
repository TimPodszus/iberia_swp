package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the lobby join user request
 *
 * @see de.uol.swp.common.lobby.message.LobbyJoinUserRequest
 * @since 2023-05-14
 */
public class LobbyJoinUserRequestTest {

    final String lobbyName = "Test";
    final UserDTO user = new UserDTO("Marco", "Marco", "Marco@Grawunder.com");


    /**
     * Test for creation of the LobbyJoinUserRequests
     *
     * This test checks if the lobbyName and the user of the LobbyJoinUserRequest gets
     * set correctly during the creation of the request
     *
     * @since 2023-05-14
     */
    @Test
    void createLobbyJoinUserRequest() {
        LobbyJoinUserRequest request = new LobbyJoinUserRequest(lobbyName, user);

        assertEquals(lobbyName, request.getName());
        assertEquals(user, request.getUser());
    }

}
