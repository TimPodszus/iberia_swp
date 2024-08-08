package de.uol.swp.common.user.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
