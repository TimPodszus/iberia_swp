package de.uol.swp.common.user.message;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the users list message
 *
 * @see de.uol.swp.common.user.message.UsersListMessage
 * @since 2023-05-14
 */
public class UsersListMessageTest {

    final ArrayList<String> users = new ArrayList<>();

    /**
     * Test for the creation of UsersListMessages
     *
     * This test checks if the user list of the UsersListMessage gets
     * set correctly during the creation of a new message
     *
     * @since 2023-05-14
     */
    @Test
    void createUserLoggedOutMessage() {
        UsersListMessage message = new UsersListMessage(users);

        assertEquals(users, message.getUsers());
    }

}
