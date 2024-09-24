package de.uol.swp.server.usermanagement;

import de.uol.swp.common.passwordHashing.PasswordHashing;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;

import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import de.uol.swp.server.usermanagement.store.UserStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class UserManagementTest
{

    private static final int NO_USERS = 10;
    private static final User userNotInStore = new UserDTO("marco" + NO_USERS, "marco" + NO_USERS);
    private static final String[][] usersArray = new String[NO_USERS][2];
    @BeforeAll
    static void setUp() {
        for (int i = 0; i < NO_USERS; i++) {
            usersArray[i][0]= ("marco" + i);
            usersArray[i][1]= ("marco" + i);
        }

    }

        String[][] getDefaultUsers () {
            return usersArray.clone();
        }


    UserManagement getDefaultManagement() {
        UserStore store = new MainMemoryBasedUserStore();
        String[][] defaultUsers = getDefaultUsers();
        for (int i = 0; i < NO_USERS; i++) {
            store.createUser(defaultUsers[i][0], defaultUsers[i][1]);
        }

        return new UserManagement(store);
    }

    @Test
    void loginUser()
    {
        UserManagement management = getDefaultManagement();
        User userToLogIn = new UserDTO(usersArray[0][0],usersArray[0][1]);

        management.login(userToLogIn.getUsername(), userToLogIn.getPassword());

        assertTrue(management.isLoggedIn(userToLogIn));
    }

    @Test
    void loginUserEmptyPassword()
    {
        UserManagement management = getDefaultManagement();
        User userToLogIn = new UserDTO(usersArray[0][0],usersArray[0][1]);

        assertThrows(SecurityException.class, () -> management.login(userToLogIn.getUsername(), ""));

        assertFalse(management.isLoggedIn(userToLogIn));
    }

    @Test
    void loginUserWrongPassword()
    {
        UserManagement management = getDefaultManagement();
        User userToLogIn = new UserDTO(usersArray[0][0],usersArray[0][1]);


        assertThrows(SecurityException.class, () -> management.login(userToLogIn.getUsername(), "marco1"));

        assertFalse(management.isLoggedIn(userToLogIn));
    }

    @Test
    void logoutUser()
    {
        UserManagement management = getDefaultManagement();
        User userToLogin = new UserDTO(usersArray[0][0],usersArray[0][1]);

        management.login(userToLogin.getUsername(), PasswordHashing.hashPassword("marco0"));

        assertTrue(management.isLoggedIn(userToLogin));

        management.logout(userToLogin);

        assertFalse(management.isLoggedIn(userToLogin));

    }

    @Test
    void createUser()
    {
        UserManagement management = getDefaultManagement();

        management.createUser(userNotInStore);

        // Creation leads not to log in
        assertFalse(management.isLoggedIn(userNotInStore));

        // Only way to test, if user is stored
        management.login(userNotInStore.getUsername(), PasswordHashing.hashPassword("marco10"));

        assertTrue(management.isLoggedIn(userNotInStore));
    }

    @Test
    void dropUser()
    {
        UserManagement management = getDefaultManagement();
        management.createUser(userNotInStore);

        management.dropUser(userNotInStore);

        assertThrows(SecurityException.class, () -> management.login(userNotInStore.getUsername(), PasswordHashing.hashPassword("marco10")));
    }

    @Test
    void dropUserNotExisting()
    {
        UserManagement management = getDefaultManagement();
        assertThrows(UserManagementException.class, () -> management.dropUser(userNotInStore));
    }

    @Test
    void createUserAlreadyExisting()
    {
        UserManagement management = getDefaultManagement();
        User userToCreate = new UserDTO(usersArray[0][0],usersArray[0][1]);

        assertThrows(UserManagementException.class, () -> management.createUser(userToCreate));

    }



    @Test
    void updateUnknownUser()
    {
        UserManagement management = getDefaultManagement();
        assertThrows(UserManagementException.class, () -> management.updateUser(userNotInStore));
    }

    @Test
    void retrieveAllUsers()
    {
        UserManagement management = getDefaultManagement();

        List<User> allUsers = management.retrieveAllUsers();
        String[][] defaultUsers = getDefaultUsers();
        List <User> defaultUsersList = new ArrayList<>();
        for (int i = 0; i < NO_USERS; i++) {
            defaultUsersList.add(new UserDTO(defaultUsers[i][0], defaultUsers[i][1]));
        }

        Collections.sort(allUsers);
        assertEquals(allUsers, defaultUsersList);


    }


}