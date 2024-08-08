package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the abstract lobby message
 *
 * @see de.uol.swp.common.lobby.message.AbstractLobbyMessage
 * @since 2023-05-14
 */
public class AbstractLobbyMessageTest {

    final String lobbyName = "Test";
    final UserDTO user = new UserDTO("Marco", "Marco", "Marco@Grawunder.com");
    final UserDTO user1 = new UserDTO("Marco1", "Marco1", "Marco1@Grawunder.com");


    /**
     * Test for creation of the AbstractLobbyMessages
     *
     * This test checks if the lobbyName and the user of the AbstractLobbyMessage gets
     * set correctly during the creation of the message
     *
     * @since 2023-05-14
     */
    @Test
    void createAbstractLobbyMessage() {
        AbstractLobbyMessage message = new AbstractLobbyMessage(lobbyName, user);

        assertEquals(lobbyName, message.getName());
        assertEquals(user, message.getUser());
    }

    /**
     * Test for set new lobbyName and new user of the AbstractLobbyMessages
     *
     * This test checks if the lobbyName and the user of the AbstractLobbyMessage gets
     * set correctly during setting new lobbyName and user of the message
     *
     * @since 2023-05-14
     */
    @Test
    void setAbstractLobbyNameAndUser() {
        AbstractLobbyMessage message = new AbstractLobbyMessage(lobbyName, user);

        assertEquals(lobbyName, message.getName());
        assertEquals(user, message.getUser());

        message.setName("Test1");
        message.setUser(user1);

        assertEquals("Test1", message.getName());
        assertEquals(user1, message.getUser());
    }

}
