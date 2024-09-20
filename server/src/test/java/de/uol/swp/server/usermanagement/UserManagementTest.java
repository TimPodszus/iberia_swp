package de.uol.swp.server.usermanagement;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.usermanagement.store.DatabaseBasedUserStore;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserManagementTest
{

    private static final int NO_USERS = 10;
    private static final List<UserDTO> users;
    private static final User userNotInStore = new UserDTO("marco" + NO_USERS, "marco" + NO_USERS);

    static {
        users = new ArrayList<>();
        for (int i = 0; i < NO_USERS; i++) {
            users.add(new UserDTO("marco" + i, "marco" + i));
        }
        Collections.sort(users);
    }

    List<UserDTO> getDefaultUsers()
    {
        return Collections.unmodifiableList(users);
    }

    UserManagement getDefaultManagement()
    {
        DatabaseBasedUserStore store = new DatabaseBasedUserStore();
        List<UserDTO> dafaultUsers = getDefaultUsers();
        dafaultUsers.forEach(u -> store.createUser(u.getUsername(), u.getPassword()));
        return new UserManagement(store);
    }

    @Test
    void loginUser()
    {
        UserManagement management = getDefaultManagement();
        User userToLogIn = users.get(0);

        management.login(userToLogIn.getUsername(), "marco0");

        assertTrue(management.isLoggedIn(userToLogIn));
    }

    @Test
    void loginUserEmptyPassword()
    {
        UserManagement management = getDefaultManagement();
        User userToLogIn = users.get(0);

        assertThrows(SecurityException.class, () -> management.login(userToLogIn.getUsername(), ""));

        assertFalse(management.isLoggedIn(userToLogIn));
    }

    @Test
    void loginUserWrongPassword()
    {
        UserManagement management = getDefaultManagement();
        User userToLogIn = users.get(0);


        assertThrows(SecurityException.class, () -> management.login(userToLogIn.getUsername(), "marco1"));

        assertFalse(management.isLoggedIn(userToLogIn));
    }

    @Test
    void logoutUser()
    {
        UserManagement management = getDefaultManagement();
        User userToLogin = users.get(0);

        management.login(userToLogin.getUsername(), "marco0");

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
        management.login(userNotInStore.getUsername(), "marco10");

        assertTrue(management.isLoggedIn(userNotInStore));
    }

    @Test
    void dropUser()
    {
        UserManagement management = getDefaultManagement();
        management.createUser(userNotInStore);

        management.dropUser(userNotInStore);

        assertThrows(SecurityException.class, () -> management.login(userNotInStore.getUsername(), "marco10"));
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
        User userToCreate = users.get(0);

        assertThrows(UserManagementException.class, () -> management.createUser(userToCreate));

    }

    @Test
    void updateUserPassword_NotLoggedIn()
    {
        UserManagement management = getDefaultManagement();
        User userToUpdate = users.get(0);
        User updatedUser = new UserDTO(userToUpdate.getUsername(), "newPassword");

        assertFalse(management.isLoggedIn(userToUpdate));
        management.updateUser(updatedUser);

        management.login(updatedUser.getUsername(), "newPassword");
        assertTrue(management.isLoggedIn(updatedUser));
    }

    @Test
    void updateUserPassword_LoggedIn()
    {
        UserManagement management = getDefaultManagement();
        User userToUpdate = users.get(0);
        User updatedUser = new UserDTO(userToUpdate.getUsername(), "newPassword");

        management.login(userToUpdate.getUsername(), "marco0");
        assertTrue(management.isLoggedIn(userToUpdate));

        management.updateUser(updatedUser);
        assertTrue(management.isLoggedIn(updatedUser));

        management.logout(updatedUser);
        assertFalse(management.isLoggedIn(updatedUser));

        management.login(updatedUser.getUsername(), "newPassword");
        assertTrue(management.isLoggedIn(updatedUser));

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

        Collections.sort(allUsers);
        assertEquals(allUsers, getDefaultUsers());


    }


}