package de.uol.swp.common.user.request;

import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    /**
     * Tests the authorizationNeeded and hashCode methods of RegisterUserRequest.
     */
    @Test
    void testAuthorizationNeededAndHashCode() {
        IUserDTO user1 = new UserDTO("user1", "password123");
        IUserDTO user2 = new UserDTO("user1", "password123");

        RegisterUserRequest request1 = new RegisterUserRequest(user1);
        RegisterUserRequest request2 = new RegisterUserRequest(user2);
        RegisterUserRequest request3 = new RegisterUserRequest(new UserDTO(
                "user2",
                "password456"
        ));

        assertFalse(request1.authorizationNeeded(), "authorizationNeeded should return false");
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

}
