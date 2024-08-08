package de.uol.swp.common.user.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
     *
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
     *
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
        request.setPassword("Test1");

        assertEquals("Marco1", request.getUsername());
        assertEquals("Test1", request.getPassword());
    }

}
