package de.uol.swp.server.cards.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FavorableTimeEventTest {

    private static final String LOBBY_ID = "LobbyID";
    private static final String USERNAME = "username";

    @Test
    void testConstructor() {
        FavorableTimeEvent event = new FavorableTimeEvent(LOBBY_ID, USERNAME);

        assertEquals(LOBBY_ID, event.getLobbyId());
        assertEquals(USERNAME, event.getUsername());
    }

    @Test
    void testEquals() {
        FavorableTimeEvent event1 = new FavorableTimeEvent(LOBBY_ID, USERNAME);
        FavorableTimeEvent event2 = new FavorableTimeEvent(LOBBY_ID, USERNAME);
        FavorableTimeEvent event3 = new FavorableTimeEvent("DifferentLobbyID", USERNAME);
        FavorableTimeEvent event4 = new FavorableTimeEvent(LOBBY_ID, "DifferentUsername");

        assertEquals(event1, event1); // Test equality with itself
        assertEquals(event1, event2); // Test equality with another object with the same values
        assertNotEquals(event1, event3); // Test inequality with different lobbyId
        assertNotEquals(event1, event4); // Test inequality with different username
        assertNotEquals(event1, null); // Test inequality with null
        assertNotEquals(event1, new Object()); // Test inequality with an object of a different class
    }

    @Test
    void testHashCode() {
        FavorableTimeEvent event1 = new FavorableTimeEvent(LOBBY_ID, USERNAME);
        FavorableTimeEvent event2 = new FavorableTimeEvent(LOBBY_ID, USERNAME);

        assertEquals(event1.hashCode(), event2.hashCode());
    }
}