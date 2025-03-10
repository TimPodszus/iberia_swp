package de.uol.swp.common.user.message;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

    /**
     * Tests the hashCode method of UsersListMessage.
     */
    @Test
    void testHashCode() {
        List<String> users1 = Arrays.asList("user1", "user2", "user3");
        List<String> users2 = Arrays.asList("user1", "user2", "user3");
        List<String> users3 = Arrays.asList("user4", "user5");

        UsersListMessage message1 = new UsersListMessage(users1);
        UsersListMessage message2 = new UsersListMessage(users2);
        UsersListMessage message3 = new UsersListMessage(users3);

        assertEquals(message1.hashCode(), message2.hashCode());
        assertNotEquals(message1.hashCode(), message3.hashCode());
    }

}
