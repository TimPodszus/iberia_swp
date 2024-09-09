package de.uol.swp.server.usermanagement.store;

import de.uol.swp.common.user.User;
import de.uol.swp.server.database.DatabaseConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseBasedUserStoreTest {

    private static DatabaseBasedUserStore userStore;


    @BeforeAll
    public static void setUp() throws Exception {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = connection.createStatement()) {
             stmt.execute("INSERT INTO User (username, password) VALUES ('testuser', 'testpassword')");
        }

        userStore = new DatabaseBasedUserStore();
    }

    @Test
     void testFindUserWithValidCredentials() {
        Optional<User> user = userStore.findUser("testuser", "testpassword");

        assertTrue(user.isPresent());
        assertEquals("testuser", user.get().getUsername());
        assertEquals("{SHA512}6eYzCXq5zrPkjsP3DuK+ukHQXVQg7+5dqF+X2XAFcnWH/aM+9P8jIgiPTHnoEzzJzZ81EvTTowPL21vFhUFaAA==", user.get()
                                                                                                                             .getPassword());
    }

    @Test
     void testFindUserWithInvalidCredentials()  {
        Optional<User> user = userStore.findUser("invaliduser", "invalidpassword");

        assertFalse(user.isPresent());
    }

    @Test
     void testFindUserByUsername() {
        Optional<User> user = userStore.findUser("testuser");

        assertTrue(user.isPresent());
        assertEquals("testuser", user.get().getUsername());
        assertEquals("{SHA512}6eYzCXq5zrPkjsP3DuK+ukHQXVQg7+5dqF+X2XAFcnWH/aM+9P8jIgiPTHnoEzzJzZ81EvTTowPL21vFhUFaAA==", user.get()
                                                                                                                             .getPassword());
    }
}