package de.uol.swp.server.usermanagement;

import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.usermanagement.management.UserManagement;
import org.greenrobot.eventbus.Subscribe;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.request.LoginRequest;
import de.uol.swp.common.user.request.LogoutRequest;
import de.uol.swp.common.user.request.RetrieveAllOnlineUsersRequest;
import de.uol.swp.common.user.response.AllOnlineUsersResponse;
import de.uol.swp.server.message.ClientAuthorizedMessage;
import de.uol.swp.server.message.ServerExceptionMessage;
import de.uol.swp.server.usermanagement.store.UserStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Test class for AuthenticationService.
 */
public class AuthenticationServiceTest extends EventBusBasedTest {

    final IUser user = new User("name", "password");
    final IUser user2 = new User("name2", "password2");
    final IUser user3 = new User("name3", "password3");

    @Mock
    UserManagement userManagement;

    @Mock
    UserStore userStore;

    AuthenticationService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userStore = mock(UserStore.class);
        userManagement = mock(UserManagement.class);
        authService = new AuthenticationService(getBus(), userManagement);
    }

    @Subscribe
    public void onEvent(ClientAuthorizedMessage e) {
        handleEvent(e);
    }

    @Subscribe
    public void onEvent(ServerExceptionMessage e) {
        handleEvent(e);
    }

    @Subscribe
    public void onEvent(UserLoggedOutMessage e) {
        handleEvent(e);
    }

    @Subscribe
    public void onEvent(AllOnlineUsersResponse e) {
        handleEvent(e);
    }

    @Test
    void loginTest() {
        final LoginRequest loginRequest = new LoginRequest(user.getUsername(), user.getPassword());
        post(loginRequest);
        verify(userManagement, atLeast(1)).login(user.getUsername(), user.getPassword());
    }

    @Test
    void loginTestFail() throws InterruptedException {
        final LoginRequest loginRequest = new LoginRequest(user.getUsername(), user.getPassword());
        when(userManagement.login(user.getUsername(), user.getPassword())).thenThrow(new RuntimeException("Test"));
        postAndWait(loginRequest);
        assertInstanceOf(ServerExceptionMessage.class, event);
    }

    @Test
    void logoutTest() {
        loginUser(user);
        final LogoutRequest logoutRequest = new LogoutRequest();
        authService.getSession(user)
                   .ifPresent(logoutRequest::setSession);
        post(logoutRequest);
        verify(userManagement, atLeast(1)).logout(user);
    }

    private void loginUser(IUser userToLogin) {
        final LoginRequest loginRequest = new LoginRequest(userToLogin.getUsername(), userToLogin.getPassword());
        when(userManagement.login(userToLogin.getUsername(), userToLogin.getPassword())).thenReturn(userToLogin);
        post(loginRequest);
    }

    @Test
    void loggedInUsers() throws InterruptedException {
        loginUser(user);
        RetrieveAllOnlineUsersRequest request = new RetrieveAllOnlineUsersRequest();
        postAndWait(request);
        assertInstanceOf(AllOnlineUsersResponse.class, event);
        assertEquals(
                1,
                ((AllOnlineUsersResponse) event).getUsers()
                                                .size()
        );
        assertEquals(
                UserMapper.toDTO(user),
                ((AllOnlineUsersResponse) event).getUsers()
                                                .get(0)
        );
    }

    @Test
    void twoLoggedInUsers() throws InterruptedException {
        loginUser(user);
        loginUser(user2);
        RetrieveAllOnlineUsersRequest request = new RetrieveAllOnlineUsersRequest();
        postAndWait(request);
        assertInstanceOf(AllOnlineUsersResponse.class, event);
        List<IUser> users = UserMapper.toUser(((AllOnlineUsersResponse) event).getUsers());
        assertEquals(2, users.size());
        assertTrue(users.contains(user));
        assertTrue(users.contains(user2));
    }

    @Test
    void loggedInUsersEmpty() throws InterruptedException {
        RetrieveAllOnlineUsersRequest request = new RetrieveAllOnlineUsersRequest();
        postAndWait(request);
        assertInstanceOf(AllOnlineUsersResponse.class, event);
        assertTrue(((AllOnlineUsersResponse) event).getUsers()
                                                   .isEmpty());
    }

    @Test
    void getSessionsForUsersTest() {
        loginUser(user);
        loginUser(user2);
        loginUser(user3);
        Set<IUser> users = new TreeSet<>();
        users.add(user);
        users.add(user2);
        users.add(user3);
        Optional<Session> session1 = authService.getSession(user);
        Optional<Session> session2 = authService.getSession(user2);
        Optional<Session> session3 = authService.getSession(user2);
        assertTrue(session1.isPresent());
        assertTrue(session2.isPresent());
        assertTrue(session3.isPresent());
        List<Session> sessions = authService.getSessions(users);
        assertEquals(3, sessions.size());
        assertTrue(sessions.contains(session1.get()));
        assertTrue(sessions.contains(session2.get()));
        assertTrue(sessions.contains(session3.get()));
    }
}