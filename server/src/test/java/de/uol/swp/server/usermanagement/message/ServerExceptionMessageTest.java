package de.uol.swp.server.usermanagement.message;

import de.uol.swp.server.message.ClientAuthorizedMessage;
import de.uol.swp.server.message.ServerExceptionMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the server exception message
 *
 * @see de.uol.swp.server.message.ServerExceptionMessage
 * @since 2023-05-14
 */
public class ServerExceptionMessageTest {

    final Exception exception = new Exception();

    /**
     * Test for the creation of ServerExceptionMessages
     *
     * This test checks if the Exception to the ServerExceptionMessage gets
     * set correctly during the creation of a new message
     *
     * @since 2023-05-14
     */
    @Test
    void createServerExceptionMessage() {
        ServerExceptionMessage message = new ServerExceptionMessage(exception);

        assertEquals(exception, message.getException());
    }

}
