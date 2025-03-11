package de.uol.swp.common.lobby.message.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for the user left lobby message
 *
 * @see UserLeftLobbyResponse
 * @since 2023-05-14
 */
class UserLeftLobbyResponseTest {

    final String lobbyName = "Test";


    /**
     * Test for creation of the UserLeftLobbyMessages
     * This test checks if the lobbyName and the user of the UserLeftLobbyMessage gets
     * set correctly during the creation of the message
     *
     * @since 2023-05-14
     */
    @Test
    void createUserLeftLobbyMessage() {
        UserLeftLobbyResponse message = new UserLeftLobbyResponse(lobbyName);

        assertEquals(lobbyName, message.getLobbyId());
    }

    /**
     * Tests the default constructor of UserLeftLobbyResponse
     */
    @Test
    void testDefaultConstructor() {
        UserLeftLobbyResponse response = new UserLeftLobbyResponse();

        assertNotNull(response);
        assertNull(response.lobbyId);
    }

}
