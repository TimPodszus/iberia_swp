package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test for the abstract lobby message
 *
 * @see de.uol.swp.common.lobby.message.AbstractLobbyMessage
 * @since 2023-05-14
 */
class AbstractLobbyMessageTest {

    final String lobbyName = "Test";
    final UserDTO user = new UserDTO("Marco", "Marco");
    final UserDTO user1 = new UserDTO("Marco1", "Marco1");


    /**
     * Test for creation of the AbstractLobbyMessages
     * This test checks if the lobbyName and the user of the AbstractLobbyMessage gets
     * set correctly during the creation of the message
     *
     * @since 2023-05-14
     */
    @Test
    void createAbstractLobbyMessage() {
        AbstractLobbyMessage message = new AbstractLobbyMessage(lobbyName, user);

        assertEquals(lobbyName, message.getLobbyId());
        assertEquals(user, message.getUser());
    }

    /**
     * Test for set new lobbyName and new user of the AbstractLobbyMessages
     * This test checks if the lobbyName and the user of the AbstractLobbyMessage gets
     * set correctly during setting new lobbyName and user of the message
     *
     * @since 2023-05-14
     */
    @Test
    void setAbstractLobbyNameAndUser() {
        AbstractLobbyMessage message = new AbstractLobbyMessage(lobbyName, user);

        assertEquals(lobbyName, message.getLobbyId());
        assertEquals(user, message.getUser());

        message.setLobbyId("Test1");
        message.setUser(user1);

        assertEquals("Test1", message.getLobbyId());
        assertEquals(user1, message.getUser());
    }

    /**
     * Tests the default constructor, equals and hashCode methods of AbstractLobbyMessage.
     */
    @Test
    void testDefaultConstructorEqualsAndHashCode() {
        IUserDTO user1 = new UserDTO("user1", "password123");
        IUserDTO user2 = new UserDTO("user1", "password123");

        AbstractLobbyMessage message1 = new AbstractLobbyMessage("lobby1", user1);
        AbstractLobbyMessage message2 = new AbstractLobbyMessage("lobby1", user2);
        AbstractLobbyMessage message3 = new AbstractLobbyMessage("lobby2", user2);
        AbstractLobbyMessage message4 = new AbstractLobbyMessage();

        assertEquals(message1, message1);
        assertEquals(message1, message2);

        assertNotEquals(message1, message3);
        assertNotEquals(null, message1);
        assertNotEquals(message1, new Object());

        assertEquals(message1.hashCode(), message2.hashCode());
        assertNotEquals(message1.hashCode(), message3.hashCode());
    }
}
