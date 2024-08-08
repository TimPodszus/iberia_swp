package de.uol.swp.common.user.response;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the login successful response
 *
 * @see de.uol.swp.common.user.response.LoginSuccessfulResponse
 * @since 2023-05-14
 */
public class LoginSuccessfulResponseTest {

    final User user = new UserDTO("Marco", "Marco", "Marco@Grawunder.com");


    /**
     * Test for creation of the LoginSuccessfulResponses
     *
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

}
