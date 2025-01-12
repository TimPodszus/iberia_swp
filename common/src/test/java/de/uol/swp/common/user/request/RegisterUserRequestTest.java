package de.uol.swp.common.user.request;

import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the register user request
 *
 * @see de.uol.swp.common.user.request.RegisterUserRequest
 * @since 2023-05-14
 */
 class RegisterUserRequestTest {

    final IUserDTO user = new UserDTO("Marco", "Marco");

    /**
     * Test for creation of the RegisterUserRequests
     * This test checks if the user of the RegisterUserRequest gets
     * set correctly during the creation of the request
     *
     * @since 2023-05-14
     */
    @Test
    void createRegisterUserRequest() {
        RegisterUserRequest request = new RegisterUserRequest(user);

        assertEquals(user, request.getUser());
    }

}
