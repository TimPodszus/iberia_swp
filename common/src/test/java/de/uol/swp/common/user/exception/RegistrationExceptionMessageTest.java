package de.uol.swp.common.user.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

    @Test
    void testHashCode() {
        RegistrationExceptionMessage msg1 = new RegistrationExceptionMessage("Error: Username taken");
        RegistrationExceptionMessage msg2 = new RegistrationExceptionMessage("Error: Username taken");
        RegistrationExceptionMessage msg3 = new RegistrationExceptionMessage("Error: Email already in use");

        assertEquals(msg1.hashCode(), msg2.hashCode());
        assertNotEquals(msg1.hashCode(), msg3.hashCode());
    }

}
