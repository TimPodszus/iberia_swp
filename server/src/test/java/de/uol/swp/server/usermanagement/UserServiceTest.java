package de.uol.swp.server.usermanagement;

import de.uol.swp.common.user.request.RegisterUserRequest;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.usermanagement.store.UserStore;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


public class UserServiceTest extends EventBusBasedTest {

    static final IUser userToRegister = new User("Marco", "Marco");
    static final IUser userWithSameName = new User("Marco", "Marco2");
    UserService userService;


    @Mock
    UserStore userStore;
    @Mock
    UserManagement userManagement;

    @BeforeEach
     void setUp() {
         MockitoAnnotations.openMocks(this);
         userManagement = new UserManagement(userStore);
         EventBus eventBus = getBus();
         userService = new UserService(eventBus, userManagement);

     }

    @Test
    void registerUserTest()  {
        final RegisterUserRequest request = new RegisterUserRequest(UserMapper.toDTO(userToRegister));
        when(userStore.findUser(userToRegister.getUsername(), userToRegister.getPassword())).thenReturn(java.util.Optional.of(userToRegister));
        // The post will lead to a call of a UserService function
        post(request);

        // can only test, if something in the state has changed
        final IUser loggedInUser = userManagement.login(userToRegister.getUsername(), userToRegister.getPassword());

        assertNotNull(loggedInUser);
        assertEquals(userToRegister, loggedInUser);
    }

    @Test
    void registerSecondUserWithSameName() {
        when(userStore.findUser(userToRegister.getUsername(), "Marco")).thenReturn(java.util.Optional.of(userToRegister));
        final RegisterUserRequest request = new RegisterUserRequest(UserMapper.toDTO(userToRegister));
        final RegisterUserRequest request2 = new RegisterUserRequest(UserMapper.toDTO(userWithSameName));

        post(request);
        post(request2);

        final IUser loggedInUser = userManagement.login(userToRegister.getUsername(), "Marco");

        // old user should be still in the store
        assertNotNull(loggedInUser);
        assertEquals(userToRegister, loggedInUser);

    }

}