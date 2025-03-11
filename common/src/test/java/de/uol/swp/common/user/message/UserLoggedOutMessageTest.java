package de.uol.swp.common.user.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test for the user logged out message
 *
 * @see de.uol.swp.common.user.message.UserLoggedOutMessage
 * @since 2023-05-14
 */
public class UserLoggedOutMessageTest {

    /**
     * Test for the creation of UserLoggedOutMessages
     *
     * This test checks if the username of the UserLoggedOutMessage gets
     * set correctly during the creation of a new message
     *
     * @since 2023-05-14
     */
    @Test
    void createUserLoggedOutMessage() {
        UserLoggedOutMessage message = new UserLoggedOutMessage("Test");

        assertEquals("Test", message.getUsername());
    }

    /**
     * Tests the constructor without parameters and hashCode method of UserLoggedOutMessage.
     */
    @Test
    void testConstructorAndHashCode() {
        UserLoggedOutMessage message1 = new UserLoggedOutMessage();
        UserLoggedOutMessage message2 = new UserLoggedOutMessage();

        assertNull(message1.getUsername());
        assertEquals(message1.hashCode(), message2.hashCode());
    }
}
