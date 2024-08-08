package de.uol.swp.common.user.exception;

import de.uol.swp.common.user.message.UserLoggedInMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the registration exception message
 *
 * @see de.uol.swp.common.user.exception.RegistrationExceptionMessage
 * @since 2023-05-14
 */
public class RegistrationExceptionMessageTest {

    /**
     * Test for the creation of RegistrationExceptionMessages
     *
     * This test checks if the exception message of the RegistrationExceptionMessage gets
     * set correctly during the creation of a new message
     *
     * @since 2023-05-14
     */
    @Test
    void createRegistrationExceptionMessage() {
        RegistrationExceptionMessage message = new RegistrationExceptionMessage("Test");

        assertEquals("RegistrationExceptionMessage Test", message.toString());
    }

}
