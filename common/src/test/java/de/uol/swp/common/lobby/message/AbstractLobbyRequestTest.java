package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the abstract lobby request
 *
 * @see de.uol.swp.common.lobby.message.AbstractLobbyRequest
 * @since 2023-05-14
 */
class AbstractLobbyRequestTest {

    final String lobbyId = "Test";
    final UserDTO user = new UserDTO("Marco", "Marco");
    final UserDTO user1 = new UserDTO("Marco1", "Marco1");


    /**
     * Test for creation of the AbstractLobbyRequests
     * This test checks if the lobbyName and the user of the AbstractLobbyRequest gets
     * set correctly during the creation of the request
     *
     * @since 2023-05-14
     */
    @Test
    void createAbstractLobbyRequest() {
        AbstractLobbyRequest request = new AbstractLobbyRequest(lobbyId);

        assertEquals(lobbyId, request.getLobbyId());
    }
}
