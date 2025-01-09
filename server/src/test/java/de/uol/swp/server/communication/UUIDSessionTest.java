package de.uol.swp.server.communication;

import de.uol.swp.common.user.Session;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.usermanagement.UserMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UUIDSessionTest
{

    final IUser user = new User("name", "password");

    @Test
    void createSessionTest()
    {
        // Creates the session of the user
        Session session = UUIDSession.create(user);

        // Checks if the session was created
        assertNotNull(session);
        assertNotNull(session.getSessionId());
        assertEquals(UserMapper.toDTO(user), session.getUser());
    }

    @Test
    void getSessionUserTest()
    {
        // Creates the session of the user
        Session session = UUIDSession.create(user);

        // Checks if the user of the session equals the user session create
        assertEquals(UserMapper.toDTO(user), session.getUser());
    }

}
