package de.uol.swp.server.cards.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MigrationOverseasEventTest {

    @Test
    void testEqualsAndHashCode() {
        MigrationOverseasEvent event1 = new MigrationOverseasEvent("lobby1", "user1");
        MigrationOverseasEvent event2 = new MigrationOverseasEvent("lobby1", "user1");
        MigrationOverseasEvent event3 = new MigrationOverseasEvent("lobby2", "user2");

        assertEquals(event1, event1);

        assertNotEquals(event1, null);

        assertNotEquals(event1, new Object());

        assertEquals(event1, event2);
        assertNotEquals(event1, event3);
        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1.hashCode(), event3.hashCode());
    }

    @Test
    void testGetters() {
        MigrationOverseasEvent event = new MigrationOverseasEvent("lobby1", "user1");

        assertEquals("lobby1", event.getLobbyId());
        assertEquals("user1", event.getUsername());
    }
}