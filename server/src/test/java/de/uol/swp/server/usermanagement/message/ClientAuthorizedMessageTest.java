package de.uol.swp.server.usermanagement.message;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.message.ClientAuthorizedMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the client authorized message
 *
 * @see de.uol.swp.server.message.ClientAuthorizedMessage
 * @since 2023-05-14
 */
public class ClientAuthorizedMessageTest {

    final User user = new UserDTO("name", "password", "email@test.de");

    /**
     * Test for the creation of ClientAuthorizedMessages
     *
     * This test checks if the user of the ClientAuthorizedMessage gets
     * set correctly during the creation of a new message
     *
     * @since 2023-05-14
     */
    @Test
    void createClientAuthorizedMessage() {
        ClientAuthorizedMessage message = new ClientAuthorizedMessage(user);

        assertEquals(user, message.getUser());
    }

}
