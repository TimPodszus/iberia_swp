package de.uol.swp.common.user.response;

import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test for the login successful response
 *
 * @see de.uol.swp.common.user.response.LoginSuccessfulResponse
 * @since 2023-05-14
 */
class LoginSuccessfulResponseTest {

    final IUserDTO user = new UserDTO("Marco", "Marco");


    /**
     * Test for creation of the LoginSuccessfulResponses
     * This test checks if the user of the LoginSuccessfulResponse gets
     * set correctly during the creation of the response
     *
     * @since 2023-05-14
     */
    @Test
    void createLoginSuccessfulResponse() {
        LoginSuccessfulResponse response = new LoginSuccessfulResponse(user);

        assertEquals(user, response.getUser());
    }

    /**
     * Tests the hashCode method of LoginSuccessfulResponse.
     */
    @Test
    void testHashCode() {
        IUserDTO user1 = new UserDTO("user1", "password1");
        IUserDTO user2 = new UserDTO("user1", "password1");
        IUserDTO user3 = new UserDTO("user2", "password2");

        LoginSuccessfulResponse response1 = new LoginSuccessfulResponse(user1);
        LoginSuccessfulResponse response2 = new LoginSuccessfulResponse(user2);
        LoginSuccessfulResponse response3 = new LoginSuccessfulResponse(user3);

        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }
}
