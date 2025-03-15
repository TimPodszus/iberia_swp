package de.uol.swp.server.usermanagement.store;

import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.usermanagement.management.UserManagementException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.Connection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DatabaseBasedUserStoreTest {


    @Mock
    static Connection connection;
    static UserStore userStore;

    @BeforeAll
    public static void setUp() {
        connection = mock(Connection.class);
        userStore = mock(UserStore.class);
        when(userStore.findUser("testuser", "testpassword")).thenReturn(Optional.of(new User("testuser",
                "testpassword"
        )));
        when(userStore.findUser("testuser")).thenReturn(Optional.of(new User("testuser", "testpassword")));
        when(userStore.createUser("newuser", "newpassword")).thenReturn(new User("newuser", "newpassword"));
        when(userStore.createUser("", "password")).thenThrow(new UserManagementException(
                "Password and username cannot be empty"));
        when(userStore.createUser("username", "")).thenThrow(new UserManagementException(
                "Password and username cannot be empty"));
    }

    @Test
    void testFindUserWithValidCredentials() {
        Optional<IUser> user = userStore.findUser("testuser", "testpassword");

        assertTrue(user.isPresent());
        assertEquals(
                "testuser",
                user.get()
                    .getUsername()
        );
        assertEquals(
                "{SHA512}6eYzCXq5zrPkjsP3DuK+ukHQXVQg7+5dqF+X2XAFcnWH/aM+9P8jIgiPTHnoEzzJzZ81EvTTowPL21vFhUFaAA==",
                user.get()
                    .getPassword()
        );
    }

    @Test
    void testFindUserWithInvalidCredentials() {
        Optional<IUser> user = userStore.findUser("invaliduser", "invalidpassword");

        assertFalse(user.isPresent());
    }

    @Test
    void testFindUserByUsername() {
        Optional<IUser> user = userStore.findUser("testuser");

        assertTrue(user.isPresent());
        assertEquals("testuser",
                user.get()
                    .getUsername()
        );
        assertEquals("{SHA512}6eYzCXq5zrPkjsP3DuK+ukHQXVQg7+5dqF+X2XAFcnWH/aM+9P8jIgiPTHnoEzzJzZ81EvTTowPL21vFhUFaAA==",
                user.get()
                    .getPassword()
        );
    }

    @Test
    void testCreateUserWithValidCredentials() {
        IUser user = userStore.createUser("newuser", "newpassword");

        assertNotNull(user);
        assertEquals("newuser", user.getUsername());
        assertEquals("{SHA512}fdKanJZD/VJOG0NglkuJzlmRTmjR/RqwTdYfuqq8WOV53P+1t0VKsB5YbIrpjlOLXW4P86591ELeczNIbcnfGg==",
                user.getPassword()
        );
    }

    @Test
    void testCreateUserWithEmptyUsername() {
        Exception exception = assertThrows(UserManagementException.class, () -> userStore.createUser("username", ""));

        String expectedMessage = "Password and username cannot be empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testCreateUserWithEmptyPassword() {
        Exception exception = assertThrows(UserManagementException.class, () -> userStore.createUser("", "password"));

        String expectedMessage = "Password and username cannot be empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}