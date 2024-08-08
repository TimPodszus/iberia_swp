package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the lobby created message
 *
 * @see de.uol.swp.common.lobby.message.LobbyCreatedMessage
 * @since 2023-05-14
 */
public class LobbyCreatedMessageTest {

    final String lobbyName = "Test";
    final UserDTO user = new UserDTO("Marco", "Marco", "Marco@Grawunder.com");


    /**
     * Test for creation of the LobbyCreatedMessages
     *
     * This test checks if the lobbyName and the user of the LobbyCreatedMessage gets
     * set correctly during the creation of the message
     *
     * @since 2023-05-14
     */
    @Test
    void createLobbyCreatedMessage() {
        LobbyCreatedMessage message = new LobbyCreatedMessage(lobbyName, user);

        assertEquals(lobbyName, message.getName());
        assertEquals(user, message.getUser());
    }

}
