package de.uol.swp.common.user.request;

import de.uol.swp.common.passwordHashing.PasswordHashing;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for the login request
 *
 * @see de.uol.swp.common.user.request.LoginRequest
 * @since 2023-05-14
 */
public class LoginRequestTest {

    final String username = "Marco";
    final String password = "Test";

    /**
     * Test for creation of the LoginRequests
     * This test checks if the username and the password of the LoginRequest gets
     * set correctly during the creation of the request
     *
     * @since 2023-05-14
     */
    @Test
    void createLoginRequest() {
        LoginRequest request = new LoginRequest(username, password);

        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());
    }

    /**
     * Test for set new username and new password of the LoginRequests
     * This test checks if the username and the password of the LoginRequest gets
     * set correctly during setting new username and password of the request
     *
     * @since 2023-05-14
     */
    @Test
    void setLobbyRequestUsernameAndPassword() {
        LoginRequest request = new LoginRequest(username, password);

        assertEquals(username, request.getUsername());
        assertEquals(password, request.getPassword());

        request.setUsername("Marco1");
        request.setPassword(PasswordHashing.hashPassword("Test1"));

        assertEquals("Marco1", request.getUsername());
        assertEquals(PasswordHashing.hashPassword("Test1"), request.getPassword());
    }

    /**
     * Tests the authorizationNeeded and hashCode methods of LoginRequest.
     */
    @Test
    void testAuthorizationNeededAndHashCode() {
        LoginRequest request1 = new LoginRequest("user1", "password123");
        LoginRequest request2 = new LoginRequest("user1", "password123");
        LoginRequest request3 = new LoginRequest("user2", "password456");

        assertFalse(request1.authorizationNeeded(), "authorizationNeeded should return false");
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

}
