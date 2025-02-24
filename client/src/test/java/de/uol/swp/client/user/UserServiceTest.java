package de.uol.swp.client.user;


import de.uol.swp.client.EventBusBasedTest;
import de.uol.swp.common.game.message.request.ChangePasswordRequest;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.request.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * This a test of the class is used to hide the communication details
 *
 * @author Marco Grawunder
 * @see de.uol.swp.client.user.UserService
 * @since 2019-10-10
 */


public class UserServiceTest extends EventBusBasedTest {

    final IUserDTO defaultUser = new UserDTO("Marco", "test");


    /**
     * Subroutine used for tests that need a logged in user
     * <p>
     * This subroutine creates a new UserService object registered to the EventBus
     * of this test class and class the objects login method for the default user.
     *
     * @throws InterruptedException thrown by lock.await()
     * @since 2019-10-10
     */
    private void loginUser() throws InterruptedException {
        UserService userService = new UserService(getBus());
        userService.login(defaultUser.getUsername(), "Test");
        waitForLock();
    }

    // handlers for events
    @Subscribe
    public void onEvent(LoginRequest e) {
        handleEvent(e);
    }

    @Subscribe
    public void onEvent(LogoutRequest e) {
        handleEvent(e);
    }

    @Subscribe
    public void onEvent(RegisterUserRequest e) {
        handleEvent(e);
    }


    @Subscribe
    public void onEvent(UpdateUserRequest e) {
        handleEvent(e);
    }

    @Subscribe
    public void onEvent(RetrieveAllOnlineUsersRequest e) {
        handleEvent(e);
    }

    /**
     * Test for the login method
     * <p>
     * This test first calls the loginUser subroutine. Afterwards it checks if a
     * LoginRequest object got posted to the EventBus and if its content is the
     * default users information.
     * The test fails if any of the checks fail.
     *
     * @throws InterruptedException thrown by loginUser()
     * @since 2019-10-10
     */
    @Test
    void loginTest() throws InterruptedException {
        loginUser();

        assertInstanceOf(LoginRequest.class, event);

        LoginRequest loginRequest = (LoginRequest) event;
        assertEquals(loginRequest.getUsername(), defaultUser.getUsername());
    }

    /**
     * Test for the logout method
     * <p>
     * This test first calls the loginUser subroutine. Afterwards it creates a new
     * UserService object registered to the EventBus of this test class. It then
     * calls the logout function of the object using the defaultUser as parameter
     * and waits for it to post an LogoutRequest object on the EventBus. It then
     * checks if authorization is needed to logout the user.
     * The test fails if no LogoutRequest is posted within one second or the request
     * says that no authorization is needed
     *
     * @throws InterruptedException thrown by loginUser() and lock.await()
     * @since 2019-10-10
     */
    @Test
    void logoutTest() throws InterruptedException {
        loginUser();
        event = null;

        UserService userService = new UserService(getBus());
        userService.logout(defaultUser);

        waitForLock();

        assertInstanceOf(LogoutRequest.class, event);

        LogoutRequest request = (LogoutRequest) event;

        assertTrue(request.authorizationNeeded());
    }

    /**
     * Test for the createUser routine
     * <p>
     * This Test creates a new UserService object registered to the EventBus of
     * this test class. It then calls the createUser function of the object using
     * the defaultUser as parameter and waits for it to post an updateUserRequest
     * object on the EventBus.
     * If this happens within one second, it checks if the user in the request object
     * is the same as the default user and if authorization is needed.
     * Authorization should not be needed.
     * If any of these checks fail or the method takes to long, this test is unsuccessful.
     *
     * @throws InterruptedException thrown by lock.await()
     * @since 2019-10-10
     */
    @Test
    void createUserTest() throws InterruptedException {
        UserService userService = new UserService(getBus());
        userService.createUser(defaultUser);

        waitForLock();

        assertInstanceOf(RegisterUserRequest.class, event);

        RegisterUserRequest request = (RegisterUserRequest) event;

        assertEquals(
                request.getUser()
                       .getUsername(), defaultUser.getUsername()
        );

        assertFalse(request.authorizationNeeded());

    }


    @Test
    void testChangePassword() {
        String newPassword = "newPassword123";
        EventBus eventBus = mock(EventBus.class);
        UserService userService = new UserService(eventBus);


        userService.changePassword(defaultUser, newPassword);

        ArgumentCaptor<ChangePasswordRequest> captor = ArgumentCaptor.forClass(ChangePasswordRequest.class);
        verify(eventBus).post(captor.capture());

        ChangePasswordRequest request = captor.getValue();
        assertEquals(defaultUser, request.getUserDTO());
        assertEquals(newPassword, request.getNewPassword());
    }

}