package de.uol.swp.common.user.request;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the update user request
 *
 * @see de.uol.swp.common.user.request.UpdateUserRequest
 * @since 2023-05-14
 */
public class UpdateUserRequestTest {

    final User user = new UserDTO("Marco", "Marco", "Marco@Grawunder.com");

    /**
     * Test for creation of the UpdateUserRequests
     *
     * This test checks if the user of the UpdateUserRequest gets
     * set correctly during the creation of the request
     *
     * @since 2023-05-14
     */
    @Test
    void createUpdateUserRequest() {
        UpdateUserRequest request = new UpdateUserRequest(user);

        assertEquals(user, request.getUser());
    }

}
