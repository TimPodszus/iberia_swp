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
public class AbstractLobbyRequestTest {

    final String lobbyName = "Test";
    final UserDTO user = new UserDTO("Marco", "Marco", "Marco@Grawunder.com");
    final UserDTO user1 = new UserDTO("Marco1", "Marco1", "Marco1@Grawunder.com");


    /**
     * Test for creation of the AbstractLobbyRequests
     *
     * This test checks if the lobbyName and the user of the AbstractLobbyRequest gets
     * set correctly during the creation of the request
     *
     * @since 2023-05-14
     */
    @Test
    void createAbstractLobbyRequest() {
        AbstractLobbyRequest request = new AbstractLobbyRequest(lobbyName, user);

        assertEquals(lobbyName, request.getName());
        assertEquals(user, request.getUser());
    }

    /**
     * Test for set new lobbyName and new user of the AbstractLobbyRequests
     *
     * This test checks if the lobbyName and the user of the AbstractLobbyRequest gets
     * set correctly during setting new lobbyName and user of the request
     *
     * @since 2023-05-14
     */
    @Test
    void setAbstractLobbyNameAndUser() {
        AbstractLobbyRequest request = new AbstractLobbyRequest(lobbyName, user);

        assertEquals(lobbyName, request.getName());
        assertEquals(user, request.getUser());

        request.setName("Test1");
        request.setUser(user1);

        assertEquals("Test1", request.getName());
        assertEquals(user1, request.getUser());
    }

}
