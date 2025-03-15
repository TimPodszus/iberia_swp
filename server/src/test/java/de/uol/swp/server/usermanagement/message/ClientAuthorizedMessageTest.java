package de.uol.swp.server.usermanagement.message;


import de.uol.swp.server.message.ClientAuthorizedMessage;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the client authorized message
 *
 * @see de.uol.swp.server.message.ClientAuthorizedMessage
 * @since 2023-05-14
 */
 class ClientAuthorizedMessageTest {

    final IUser user = new User("name", "password");

    /**
     * Test for the creation of ClientAuthorizedMessages
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
