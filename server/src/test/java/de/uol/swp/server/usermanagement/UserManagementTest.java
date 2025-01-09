package de.uol.swp.server.usermanagement;

import de.uol.swp.common.passwordHashing.PasswordHashing;

import de.uol.swp.server.usermanagement.store.UserStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserManagementTest {

    private static final int NO_USERS = 10;
    private static final IUser userNotInStore = new User("marco" + NO_USERS, "marco" + NO_USERS);
    private static final String[][] usersArray = new String[NO_USERS][2];

    @Mock
    UserStore userStore;
    @Mock
    UserManagement userManagement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userManagement = new UserManagement(userStore);
        for (int i = 0; i < NO_USERS; i++) {
            usersArray[i][0] = ("marco" + i);
            usersArray[i][1] = ("marco" + i);
        }
    }


    @Test
    void loginUser()
    {
        IUser userToLogIn = new User(usersArray[0][0],usersArray[0][1]);
        IUser mockUser = new User(usersArray[0][0], PasswordHashing.hashPassword(usersArray[0][1]));

        when(userStore.findUser(userToLogIn.getUsername(), userToLogIn.getPassword())).thenReturn(java.util.Optional.of(mockUser));
        userManagement.login(userToLogIn.getUsername(), userToLogIn.getPassword());

        assertTrue(userManagement.isLoggedIn(userToLogIn));
    }



    @Test
    void loginUserEmptyPassword()
    {

        IUser userToLogIn = new User(usersArray[0][0],usersArray[0][1]);

        assertThrows(SecurityException.class, () -> userManagement.login(userToLogIn.getUsername(), ""));

        assertFalse(userManagement.isLoggedIn(userToLogIn));
    }

    @Test
    void loginUserWrongPassword()
    {
        IUser userToLogIn = new User(usersArray[0][0],usersArray[0][1]);

        assertThrows(SecurityException.class, () -> userManagement.login(userToLogIn.getUsername(), "marco1"));

        assertFalse(userManagement.isLoggedIn(userToLogIn));
    }

    @Test
    void logoutUser()
    {
        IUser mockUser = new User(usersArray[0][0], PasswordHashing.hashPassword(usersArray[0][1]));

        when(userStore.findUser(usersArray[0][0], PasswordHashing.hashPassword(usersArray[0][1]))).thenReturn(java.util.Optional.of(mockUser));
        IUser userToLogin = new User(usersArray[0][0],usersArray[0][1]);

        userManagement.login(userToLogin.getUsername(), PasswordHashing.hashPassword("marco0"));

        assertTrue(userManagement.isLoggedIn(userToLogin));

        userManagement.logout(userToLogin);

        assertFalse(userManagement.isLoggedIn(userToLogin));

    }

    @Test
    void createUser()
    {
        userManagement.createUser(userNotInStore);
        // Creation leads not to log in
        assertFalse(userManagement.isLoggedIn(userNotInStore));

        when(userStore.findUser(userNotInStore.getUsername(), PasswordHashing.hashPassword("marco10"))).thenReturn(java.util.Optional.of(userNotInStore));

        // Only way to test, if user is stored
        userManagement.login(userNotInStore.getUsername(), PasswordHashing.hashPassword("marco10"));

        assertTrue(userManagement.isLoggedIn(userNotInStore));
    }

    @Test
    void dropUser()
    {
        userManagement.createUser(userNotInStore);
        IUser mockUser = new User(userNotInStore.getUsername(), PasswordHashing.hashPassword(userNotInStore.getPassword()));

        when(userStore.findUser(userNotInStore.getUsername())).thenReturn(java.util.Optional.of(mockUser));
        userManagement.dropUser(userNotInStore);

        assertThrows(SecurityException.class, () -> userManagement.login(userNotInStore.getUsername(), PasswordHashing.hashPassword(
                "marco10")));
    }

    @Test
    void dropUserNotExisting()
    {

        assertThrows(UserManagementException.class, () -> userManagement.dropUser(userNotInStore));
    }

    @Test
    void createExistingUser(){
        IUser userToCreate = new User(usersArray[0][0],usersArray[0][1]);
        IUser mockUser = new User(usersArray[0][0], PasswordHashing.hashPassword(usersArray[0][1]));

        when(userStore.findUser(usersArray[0][0])).thenReturn(java.util.Optional.of(mockUser));

        assertThrows(UserManagementException.class, () -> userManagement.createUser(userToCreate));
    }


    @Test
    void updateUnknownUser()
    {
        assertThrows(UserManagementException.class, () -> userManagement.updateUser(userNotInStore));
    }




}