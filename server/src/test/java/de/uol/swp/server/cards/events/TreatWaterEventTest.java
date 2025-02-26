package de.uol.swp.server.cards.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreatWaterEventTest {

    @Test
    void testConstructorAndGetters() {
        String lobbyId = "lobby1";
        String username = "user1";
        TreatWaterEvent event = new TreatWaterEvent(lobbyId, username);

        assertEquals(lobbyId, event.getLobbyId());
        assertEquals(username, event.getUsername());
    }

    @Test
    void testEquals() {
        String lobbyId = "lobby1";
        String username = "user1";
        TreatWaterEvent event1 = new TreatWaterEvent(lobbyId, username);
        TreatWaterEvent event2 = new TreatWaterEvent(lobbyId, username);
        TreatWaterEvent event3 = new TreatWaterEvent("lobby2", "user2");

        assertEquals(event1, event2);
        assertNotEquals(event1, event3);
    }

    @Test
    void testEqualsWithSameObject() {
        TreatWaterEvent event = new TreatWaterEvent("lobby1", "user1");

        assertEquals(event, event);
    }

    @Test
    void testEqualsWithNull() {
        TreatWaterEvent event = new TreatWaterEvent("lobby1", "user1");

        assertNotEquals(null, event);
    }

    @Test
    void testEqualsWithDifferentClass() {
        TreatWaterEvent event = new TreatWaterEvent("lobby1", "user1");

        assertNotEquals(new Object(), event);
    }

    @Test
    void testHashCode() {
        String lobbyId = "lobby1";
        String username = "user1";
        TreatWaterEvent event1 = new TreatWaterEvent(lobbyId, username);
        TreatWaterEvent event2 = new TreatWaterEvent(lobbyId, username);
        TreatWaterEvent event3 = new TreatWaterEvent("lobby2", "user2");

        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1.hashCode(), event3.hashCode());
    }
}