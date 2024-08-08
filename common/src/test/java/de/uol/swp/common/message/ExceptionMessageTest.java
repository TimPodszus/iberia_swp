package de.uol.swp.common.message;

import de.uol.swp.common.user.exception.RegistrationExceptionMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the exception message
 *
 * @see de.uol.swp.common.message.ExceptionMessage
 * @since 2023-05-14
 */
public class ExceptionMessageTest {

    /**
     * Test for the creation of ExceptionMessages
     *
     * This test checks if the exception message of the ExceptionMessage gets
     * set correctly during the creation of a new message
     *
     * @since 2023-05-14
     */
    @Test
    void createRegistrationExceptionMessage() {
        ExceptionMessage message = new ExceptionMessage("Test");

        assertEquals("Test", message.getException());
    }

}
