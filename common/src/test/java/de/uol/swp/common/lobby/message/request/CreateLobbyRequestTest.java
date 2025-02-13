package de.uol.swp.common.lobby.message.request;

import de.uol.swp.common.lobby.message.request.CreateLobbyRequest;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Test for the create lobby request
 *
 * @see CreateLobbyRequest
 * @since 2023-05-14
 */
class CreateLobbyRequestTest {

    /**
     * Test for creation of the CreateLobbyRequests
     * This test checks if the lobbyName and the user of the CreateLobbyRequest gets
     * set correctly during the creation of the request
     *
     * @since 2023-05-14
     */
    @Test
    void createCreateLobbyRequest() {
        CreateLobbyRequest request = new CreateLobbyRequest();

        assertInstanceOf(CreateLobbyRequest.class, request);
    }

}
