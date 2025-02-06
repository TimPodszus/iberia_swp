package de.uol.swp.common.lobby.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the lobby leave user request
 *
 * @see LobbyLeaveUserRequest
 * @since 2023-05-14
 */
class LobbyLeaveUserRequestTest {

    final String lobbyId = "Test";


    /**
     * Test for creation of the LobbyLeaveUserRequests
     * This test checks if the lobbyName and the user of the LobbyLeaveUserRequest gets
     * set correctly during the creation of the request
     *
     * @since 2023-05-14
     */
    @Test
    void createLobbyLeaveUserRequest() {
        LobbyLeaveUserRequest request = new LobbyLeaveUserRequest(lobbyId);

        assertEquals(lobbyId, request.getLobbyId());
    }

}
