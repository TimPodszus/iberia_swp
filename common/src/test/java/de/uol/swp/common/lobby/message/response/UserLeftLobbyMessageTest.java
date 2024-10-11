package de.uol.swp.common.lobby.message.response;

import de.uol.swp.common.lobby.message.response.UserLeftLobbyMessage;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the user left lobby message
 *
 * @see UserLeftLobbyMessage
 * @since 2023-05-14
 */
class UserLeftLobbyMessageTest {

    final String lobbyName = "Test";
    final UserDTO user = new UserDTO("Marco", "Marco");


    /**
     * Test for creation of the UserLeftLobbyMessages
     * This test checks if the lobbyName and the user of the UserLeftLobbyMessage gets
     * set correctly during the creation of the message
     *
     * @since 2023-05-14
     */
    @Test
    void createUserLeftLobbyMessage() {
        UserLeftLobbyMessage message = new UserLeftLobbyMessage(lobbyName, user);

        assertEquals(lobbyName, message.getLobbyCode());
        assertEquals(user, message.getUser());
    }

}
