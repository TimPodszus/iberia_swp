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
    void testEquals() {
        String lobbyId = "lobby123";
        String username = "player1";
        AnotherDayEvent event1 = new AnotherDayEvent(lobbyId, username);
        AnotherDayEvent event2 = new AnotherDayEvent(lobbyId, username);

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void testNotEquals() {
        String lobbyId1 = "lobby123";
        String username1 = "player1";
        String lobbyId2 = "lobby456";
        String username2 = "player2";
        AnotherDayEvent event1 = new AnotherDayEvent(lobbyId1, username1);
        AnotherDayEvent event2 = new AnotherDayEvent(lobbyId2, username2);

        assertNotEquals(event1, event2);
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void testEqualsWithDifferentAmountOfActions() {
        String lobbyId = "lobby123";
        String username = "player1";
        AnotherDayEvent event1 = new AnotherDayEvent(lobbyId, username);
        AnotherDayEvent event2 = new AnotherDayEvent(lobbyId, username) {
            @Override
            public int getAmountOfActions() {
                return 3;
            }
        };

        assertNotEquals(event1, event2);
    }

    @Test
    void testEqualsWithNull() {
        String lobbyId = "lobby123";
        String username = "player1";
        AnotherDayEvent event = new AnotherDayEvent(lobbyId, username);

        assertNotEquals(null, event);
    }

    @Test
    void testEqualsWithDifferentClass() {
        String lobbyId = "lobby123";
        String username = "player1";
        AnotherDayEvent event = new AnotherDayEvent(lobbyId, username);

        assertNotEquals(new Object(), event);
    }

    @Test
    void testEqualsWithSameObject() {
        String lobbyId = "lobby123";
        String username = "player1";
        AnotherDayEvent event = new AnotherDayEvent(lobbyId, username);

        assertEquals(event, event);
    }

    @Test
    void testEqualsWithDifferentLobbyId() {
        String lobbyId1 = "lobby123";
        String username = "player1";
        String lobbyId2 = "lobby456";
        AnotherDayEvent event1 = new AnotherDayEvent(lobbyId1, username);
        AnotherDayEvent event2 = new AnotherDayEvent(lobbyId2, username);

        assertNotEquals(event1, event2);
    }

    @Test
    void testEqualsWithDifferentUsername() {
        String lobbyId = "lobby123";
        String username1 = "player1";
        String username2 = "player2";
        AnotherDayEvent event1 = new AnotherDayEvent(lobbyId, username1);
        AnotherDayEvent event2 = new AnotherDayEvent(lobbyId, username2);

        assertNotEquals(event1, event2);
    }
}