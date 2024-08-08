package de.uol.swp.common.user.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the user logged in message
 *
 * @see de.uol.swp.common.user.message.UserLoggedInMessage
 * @since 2023-05-14
 */
public class UserLoggedInMessageTest {

    /**
     * Test for the creation of UserLoggedInMessages
     *
     * This test checks if the username of the UserLoggedInMessage gets
     * set correctly during the creation of a new message
     *
     * @since 2023-05-14
     */
    @Test
    void createUserLoggedInMessage() {
        UserLoggedInMessage message = new UserLoggedInMessage("Test");

        assertEquals("Test", message.getUsername());
    }

}
