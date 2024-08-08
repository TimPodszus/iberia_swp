package de.uol.swp.server.usermanagement.store;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MainMemoryBasedUserStoreTest {

    private static final int NO_USERS = 10;
    private static final List<UserDTO> users;

    static {
        users = new ArrayList<>();
        for (int i = 0; i < NO_USERS; i++) {
            users.add(new UserDTO("marco" + i, "marco" + i, "marco" + i + "@grawunder.de"));
        }
        Collections.sort(users);
    }

    List<UserDTO> getDefaultUsers() {
        return Collections.unmodifiableList(users);
    }

    MainMemoryBasedUserStore getDefaultStore() {
        MainMemoryBasedUserStore store = new MainMemoryBasedUserStore();
        List<UserDTO> users = getDefaultUsers();
        users.forEach(u -> store.createUser(u.getUsername(), u.getPassword(), u.getEMail()));
        return store;
    }

    @Test
    void findUserByName() {
        // arrange
        UserStore store = getDefaultStore();
        User userToCreate = getDefaultUsers().get(0);

        // act
        Optional<User> userFound = store.findUser(userToCreate.getUsername());

        // assert
        assertTrue(userFound.isPresent());
        assertEquals(userToCreate, userFound.get());
        assertEquals("",userFound.get().getPassword());
    }

    @Test
    void findUserByName_NotFound() {
        UserStore store = getDefaultStore();
        User userToFind = getDefaultUsers().get(0);

        Optional<User> userFound = store.findUser("öööö" + userToFind.getUsername());

        assertFalse(userFound.isPresent());
    }

    @Test
    void findUserByNameAndPassword() {
        UserStore store = getDefaultStore();
        User userToCreate = getDefaultUsers().get(1);
        store.createUser(userToCreate.getUsername(), userToCreate.getPassword(), userToCreate.getEMail());

        Optional<User> userFound = store.findUser(userToCreate.getUsername(), userToCreate.getPassword());

        assertTrue(userFound.isPresent());
        assertEquals(userToCreate, userFound.get());
        assertEquals("",userFound.get().getPassword());
    }

    @Test
    void findUserByNameAndPassword_NotFound() {
        UserStore store = getDefaultStore();
        User userToFind = getDefaultUsers().get(0);

        Optional<User> userFound = store.findUser(userToFind.getUsername(), "");

        assertFalse(userFound.isPresent());
    }

    @Test
    void findUserByNameAndPassword_EmptyUser_NotFound() {
        UserStore store = getDefaultStore();

        Optional<User> userFound = store.findUser(null, "");

        assertFalse(userFound.isPresent());
    }


    @Test
    void overwriteUser() {
        UserStore store = getDefaultStore();
        User userToCreate = getDefaultUsers().get(1);
        store.createUser(userToCreate.getUsername(), userToCreate.getPassword(), userToCreate.getEMail());
        store.createUser(userToCreate.getUsername(), userToCreate.getPassword(), userToCreate.getEMail());

        Optional<User> userFound = store.findUser(userToCreate.getUsername(), userToCreate.getPassword());

        assertEquals(NO_USERS, store.getAllUsers().size());
        assertTrue(userFound.isPresent());
        assertEquals(userToCreate, userFound.get());

    }


    @Test
    void updateUser() {
        UserStore store = getDefaultStore();
        User userToUpdate = getDefaultUsers().get(2);

        store.updateUser(userToUpdate.getUsername(), userToUpdate.getPassword() , userToUpdate.getEMail()+"@TESTING");

        Optional<User> userFound = store.findUser(userToUpdate.getUsername());

        assertTrue(userFound.isPresent());
        assertEquals(userFound.get().getEMail(), userToUpdate.getEMail() + "@TESTING");

    }

    @Test
    void changePassword() {
        UserStore store = getDefaultStore();
        User userToUpdate = getDefaultUsers().get(2);

        store.updateUser(userToUpdate.getUsername(), userToUpdate.getPassword() +"_NEWPASS", userToUpdate.getEMail());

        Optional<User> userFound = store.findUser(userToUpdate.getUsername(), userToUpdate.getPassword() +"_NEWPASS");

        assertTrue(userFound.isPresent());
        assertEquals(userFound.get().getEMail(), userToUpdate.getEMail() );

    }

    @Test
    void dropUser() {
        UserStore store = getDefaultStore();
        User userToRemove = getDefaultUsers().get(3);

        store.removeUser(userToRemove.getUsername());

        Optional<User> userFound = store.findUser(userToRemove.getUsername());

        assertFalse(userFound.isPresent());
    }

    @Test
    void createEmptyUser(){
        UserStore store = getDefaultStore();

        assertThrows(IllegalArgumentException.class,
                () -> store.createUser("","","")
                );
    }

    @Test
    void getAllUsers() {
        UserStore store = getDefaultStore();
        List<UserDTO> allUsers = getDefaultUsers();

        List<User> allUsersFromStore = store.getAllUsers();

        allUsersFromStore.forEach(u -> assertEquals("",u.getPassword()));
        Collections.sort(allUsersFromStore);
        assertEquals(allUsers, allUsersFromStore);
    }
}