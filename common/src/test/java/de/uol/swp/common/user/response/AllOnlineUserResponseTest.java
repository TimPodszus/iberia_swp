package de.uol.swp.common.user.response;


import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for the all online user response
 *
 * @see de.uol.swp.common.user.response.AllOnlineUsersResponse
 * @since 2023-05-14
 */
class AllOnlineUserResponseTest {

    final ArrayList<IUserDTO> users = new ArrayList<>();

    /**
     * Test for creation of the AllOnlineUserResponses
     *
     * This test checks if the user list of the AllOnlineUserResponse gets
     * set correctly during the creation of the response
     *
     * @since 2023-05-14
     */
    @Test
    void createAllOnlineUsersResponse() {
        AllOnlineUsersResponse response = new AllOnlineUsersResponse(users);

        assertEquals(users, response.getUsers());
    }

    /**
     * Tests the default constructor, equals and hashCode methods of AllOnlineUsersResponse.
     */
    @Test
    void testEqualsAndHashCode() {
        IUserDTO user1 = new UserDTO("user1", "password1");
        IUserDTO user2 = new UserDTO("user2", "password2");

        AllOnlineUsersResponse response1 = new AllOnlineUsersResponse(Arrays.asList(user1, user2));
        AllOnlineUsersResponse response2 = new AllOnlineUsersResponse(Arrays.asList(user1, user2));
        AllOnlineUsersResponse response3 = new AllOnlineUsersResponse(Arrays.asList(user1));
        AllOnlineUsersResponse response4 = new AllOnlineUsersResponse();

        assertNotNull(response4);
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

}
