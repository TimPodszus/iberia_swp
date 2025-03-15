package de.uol.swp.server.usermanagement;

import de.uol.swp.common.game.message.request.ChangePasswordRequest;
import de.uol.swp.common.user.request.RegisterUserRequest;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.usermanagement.management.UserManagement;
import de.uol.swp.server.usermanagement.store.UserStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the UserService class.
 */
class UserServiceTest extends EventBusBasedTest {

    static final IUser userToRegister = new User("Marco", "Marco");
    static final IUser userWithSameName = new User("Marco", "Marco2");

    UserService userService;

    @Mock
    UserStore userStore;
    @Mock
    UserManagement userManagement;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(getBus(), userManagement);
    }

    /**
     * Tests the registration of a user.
     */
    @Test
    void registerUserTest() {
        final RegisterUserRequest request = new RegisterUserRequest(UserMapper.toDTO(userToRegister));
        when(userStore.findUser(
                userToRegister.getUsername(),
                userToRegister.getPassword()
        )).thenReturn(java.util.Optional.of(userToRegister));
        when(userManagement.login(
                userToRegister.getUsername(),
                userToRegister.getPassword()
        )).thenReturn(userToRegister);

        post(request);

        final IUser loggedInUser = userManagement.login(userToRegister.getUsername(), userToRegister.getPassword());

        assertNotNull(loggedInUser);
        assertEquals(userToRegister, loggedInUser);
    }

    /**
     * Tests the registration of a second user with the same name.
     */
    @Test
    void registerSecondUserWithSameName() {
        when(userStore.findUser(
                userToRegister.getUsername(),
                "Marco"
        )).thenReturn(java.util.Optional.of(userToRegister));
        when(userManagement.login(userToRegister.getUsername(), "Marco")).thenReturn(userToRegister);

        final RegisterUserRequest request = new RegisterUserRequest(UserMapper.toDTO(userToRegister));
        final RegisterUserRequest request2 = new RegisterUserRequest(UserMapper.toDTO(userWithSameName));

        post(request);
        post(request2);

        final IUser loggedInUser = userManagement.login(userToRegister.getUsername(), "Marco");

        // old user should be still in the store
        assertNotNull(loggedInUser);
        assertEquals(userToRegister, loggedInUser);
    }

    /**
     * Tests the change of a user's password.
     */
    @Test
    void changePasswordTest() {
        final String newPassword = "newPassword123";
        final ChangePasswordRequest request = new ChangePasswordRequest(UserMapper.toDTO(userToRegister), newPassword);

        userService.onChangePasswordEvent(request);

        // Verify that the password was changed
        verify(userManagement).changePassword(userToRegister.getUsername(), newPassword);
    }
}