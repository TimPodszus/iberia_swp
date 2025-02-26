package de.uol.swp.server.cards.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnotherDayEventTest {

    @Test
    void testConstructorAndGetters() {
        String lobbyId = "lobby123";
        String username = "player1";
        AnotherDayEvent event = new AnotherDayEvent(lobbyId, username);

        assertEquals(lobbyId, event.getLobbyId());
        assertEquals(username, event.getUsername());
        assertEquals(2, event.getAmountOfActions());
    }

    @Test
    void testEqualsAndHashCode() {
        String lobbyId = "lobby123";
        String username = "player1";
        AnotherDayEvent event1 = new AnotherDayEvent(lobbyId, username);
        AnotherDayEvent event2 = new AnotherDayEvent(lobbyId, username);
        AnotherDayEvent event3 = new AnotherDayEvent("lobby456", "player2");

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1, event3);
        assertNotEquals(event1.hashCode(), event3.hashCode());
    }

    @Test
    void testEqualsWithNull() {
        AnotherDayEvent event = new AnotherDayEvent("lobby123", "player1");

        assertNotEquals(null, event);
    }

    @Test
    void testEqualsWithDifferentClass() {
        AnotherDayEvent event = new AnotherDayEvent("lobby123", "player1");

        assertNotEquals(new Object(), event);
    }

    @Test
    void testEqualsWithSameObject() {
        AnotherDayEvent event = new AnotherDayEvent("lobby123", "player1");

        assertEquals(event, event);
    }
}