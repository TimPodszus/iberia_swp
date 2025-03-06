package de.uol.swp.server.cards.events;

import de.uol.swp.common.user.Session;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecondChanceEventTest {

    @Test
    void testConstructorAndGetters() {
        SecondChanceEvent event = new SecondChanceEvent("lobby123", "testUser");

        assertEquals("lobby123", event.getLobbyId());
        assertEquals("testUser", event.getUsername());
    }

    @Test
    void testEquals_SameObject() {
        SecondChanceEvent event = new SecondChanceEvent("lobby123", "testUser");
        assertEquals(event, event);
    }

    @Test
    void testEquals_EqualObjects() {
        SecondChanceEvent event1 = new SecondChanceEvent("lobby123", "testUser");
        SecondChanceEvent event2 = new SecondChanceEvent("lobby123", "testUser");

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void testEquals_DifferentLobbyId() {
        SecondChanceEvent event1 = new SecondChanceEvent("lobby123", "testUser");
        SecondChanceEvent event2 = new SecondChanceEvent("lobby456", "testUser");

        assertNotEquals(event1, event2);
    }

    @Test
    void testEquals_DifferentUsername() {
        SecondChanceEvent event1 = new SecondChanceEvent("lobby123", "testUser");
        SecondChanceEvent event2 = new SecondChanceEvent("lobby123", "otherUser");

        assertNotEquals(event1, event2);
    }

    @Test
    void testEquals_NullObject() {
        SecondChanceEvent event = new SecondChanceEvent("lobby123", "testUser");
        SecondChanceEvent event2 = new SecondChanceEvent("lobby123", "testUser");
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        event2.setSession(session);

        assertNotEquals(null, event);
    }

    @Test
    void testEquals_DifferentClass() {
        SecondChanceEvent event = new SecondChanceEvent("lobby123", "testUser");

        assertNotEquals(event, "Some String");
    }

    @Test
    void testEquals_superNotEquals() {
        SecondChanceEvent event = new SecondChanceEvent("lobby123", "testUser");

        assertNotEquals(event, "Some String");
    }


    @Test
    void testHashCode_DifferentObjectsSameValues() {
        SecondChanceEvent event1 = new SecondChanceEvent("lobby123", "testUser");
        SecondChanceEvent event2 = new SecondChanceEvent("lobby123", "testUser");

        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void testHashCode_DifferentValues() {
        SecondChanceEvent event1 = new SecondChanceEvent("lobby123", "testUser");
        SecondChanceEvent event2 = new SecondChanceEvent("lobby123", "otherUser");

        assertNotEquals(event1.hashCode(), event2.hashCode());
    }
}
