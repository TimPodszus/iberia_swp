package de.uol.swp.server.usermanagement.communication;

import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.communication.UUIDSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("UnstableApiUsage")
public class UUIDSessionTest {

    final User user = new UserDTO("name", "password", "email@test.de");

    @Test
    void createSessionTest() {
        // Creates the session of the user
        Session session = UUIDSession.create(user);

        // Checks if the session was created
        assertNotNull(session);
        assertNotNull(session.getSessionId());
        assertEquals(user, session.getUser());
    }

    @Test
    void getSessionUserTest() {
        // Creates the session of the user
        Session session = UUIDSession.create(user);

        // Checks if the user of the session equals the user session create
        assertEquals(user, session.getUser());
    }

}
