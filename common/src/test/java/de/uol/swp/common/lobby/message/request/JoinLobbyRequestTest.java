package de.uol.swp.common.lobby.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the lobby join user request
 *
 * @see JoinLobbyRequest
 * @since 2023-05-14
 */
class JoinLobbyRequestTest {

    final String lobbyId = "Test";


    /**
     * Test for creation of the LobbyJoinUserRequests
     * This test checks if the lobbyName and the user of the LobbyJoinUserRequest gets
     * set correctly during the creation of the request
     *
     * @since 2023-05-14
     */
    @Test
    void createLobbyJoinUserRequest() {
        JoinLobbyRequest request = new JoinLobbyRequest(lobbyId);

        assertEquals(lobbyId, request.getLobbyId());
    }

}
