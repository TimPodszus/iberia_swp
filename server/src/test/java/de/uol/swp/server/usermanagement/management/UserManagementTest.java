package de.uol.swp.server.usermanagement.management;

import de.uol.swp.common.passwordHashing.PasswordHashing;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.usermanagement.store.UserStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the UserManagement class.
 */
class UserManagementTest {

    private static final int NO_USERS = 10;
    private static final IUser userNotInStore = new User("marco" + NO_USERS, "marco" + NO_USERS);
    private static final String[][] usersArray = new String[NO_USERS][2];

    @Mock
    UserStore userStore;
    @Mock
    UserManagement userManagement;

    /**
     * Sets up the test environment before each test.
     * Initializes the UserManagement instance and populates the usersArray.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userManagement = new UserManagement(userStore);
        for (int i = 0; i < NO_USERS; i++) {
            usersArray[i][0] = ("marco" + i);
            usersArray[i][1] = ("marco" + i);
        }
    }

    /**
     * Tests the login functionality with valid credentials.
     * Verifies that the user is logged in successfully.
     */
    @Test
    void loginUser() {
        IUser userToLogIn = new User(usersArray[0][0], usersArray[0][1]);
        IUser mockUser = new User(usersArray[0][0], PasswordHashing.hashPassword(usersArray[0][1]));

        when(userStore.findUser(userToLogIn.getUsername(), userToLogIn.getPassword())).thenReturn(java.util.Optional.of(
                mockUser));
        userManagement.login(userToLogIn.getUsername(), userToLogIn.getPassword());

        assertTrue(userManagement.isLoggedIn(userToLogIn));
    }

    /**
     * Tests the login functionality with an empty password.
     * Verifies that a SecurityException is thrown and the user is not logged in.
     */
    @Test
    void loginUserEmptyPassword() {
        IUser userToLogIn = new User(usersArray[0][0], usersArray[0][1]);

        assertThrows(SecurityException.class, () -> userManagement.login(userToLogIn.getUsername(), ""));

        assertFalse(userManagement.isLoggedIn(userToLogIn));
    }

    /**
     * Tests the login functionality with an incorrect password.
     * Verifies that a SecurityException is thrown and the user is not logged in.
     */
    @Test
    void loginUserWrongPassword() {
        IUser userToLogIn = new User(usersArray[0][0], usersArray[0][1]);

        assertThrows(SecurityException.class, () -> userManagement.login(userToLogIn.getUsername(), "marco1"));

        assertFalse(userManagement.isLoggedIn(userToLogIn));
    }

    /**
     * Tests the logout functionality.
     * Verifies that the user is logged out successfully.
     */
    @Test
    void logoutUser() {
        IUser mockUser = new User(usersArray[0][0], PasswordHashing.hashPassword(usersArray[0][1]));

        when(userStore.findUser(
                usersArray[0][0],
                PasswordHashing.hashPassword(usersArray[0][1])
        )).thenReturn(java.util.Optional.of(mockUser));
        IUser userToLogin = new User(usersArray[0][0], usersArray[0][1]);

        userManagement.login(userToLogin.getUsername(), PasswordHashing.hashPassword("marco0"));

        assertTrue(userManagement.isLoggedIn(userToLogin));

        userManagement.logout(userToLogin);

        assertFalse(userManagement.isLoggedIn(userToLogin));
    }

    /**
     * Tests the createUser functionality.
     * Verifies that the user is created but not logged in.
     */
    @Test
    void createUser() {
        userManagement.createUser(userNotInStore);
        // Creation leads not to log in
        assertFalse(userManagement.isLoggedIn(userNotInStore));

        when(userStore.findUser(
                userNotInStore.getUsername(),
                PasswordHashing.hashPassword("marco10")
        )).thenReturn(java.util.Optional.of(userNotInStore));

        // Only way to test, if user is stored
        userManagement.login(userNotInStore.getUsername(), PasswordHashing.hashPassword("marco10"));

        assertTrue(userManagement.isLoggedIn(userNotInStore));
    }


    /**
     * Tests the createUser functionality with an existing user.
     * Verifies that a UserManagementException is thrown.
     */
    @Test
    void createExistingUser() {
        IUser userToCreate = new User(usersArray[0][0], usersArray[0][1]);
        IUser mockUser = new User(usersArray[0][0], PasswordHashing.hashPassword(usersArray[0][1]));

        when(userStore.findUser(usersArray[0][0])).thenReturn(java.util.Optional.of(mockUser));

        assertThrows(UserManagementException.class, () -> userManagement.createUser(userToCreate));
    }


    /**
     * Tests the getUser functionality.
     * Verifies that the correct user is retrieved.
     */
    @Test
    void testGetUser() {
        IUser user = new User(userNotInStore.getUsername(), PasswordHashing.hashPassword(userNotInStore.getPassword()));
        when(userStore.findUser(userNotInStore.getUsername())).thenReturn(java.util.Optional.of(user));

        assertEquals(user, userManagement.getUser(userNotInStore.getUsername()));
    }


    /**
     * Tests the changePassword functionality.
     * Verifies that the password is changed successfully.
     */
    @Test
    void changePasswordTest() {
        String username = "testUser";
        String newPassword = "newPassword123";

        userManagement.changePassword(username, newPassword);

        verify(userStore).updateUser(username, newPassword);
    }
}