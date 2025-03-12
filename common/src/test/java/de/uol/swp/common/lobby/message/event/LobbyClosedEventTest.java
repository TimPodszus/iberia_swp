package de.uol.swp.common.lobby.message.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyClosedEventTest {
    @Test
    void equalsReturnsTrueForSameLobbyId() {
        LobbyClosedEvent event1 = new LobbyClosedEvent("lobby1");
        LobbyClosedEvent event2 = new LobbyClosedEvent("lobby1");
        assertEquals(event1, event2);
    }

    @Test
    void equalsReturnsFalseForDifferentLobbyId() {
        LobbyClosedEvent event1 = new LobbyClosedEvent("lobby1");
        LobbyClosedEvent event2 = new LobbyClosedEvent("lobby2");
        assertNotEquals(event1, event2);
    }

    @Test
    void equalsReturnsFalseForNull() {
        LobbyClosedEvent event = new LobbyClosedEvent("lobby1");
        assertNotEquals(event, null);
    }

    @Test
    void equalsReturnsFalseForDifferentClass() {
        LobbyClosedEvent event = new LobbyClosedEvent("lobby1");
        Object obj = new Object();
        assertNotEquals(event, obj);
    }

    @Test
    void hashCodeReturnsSameValueForSameLobbyId() {
        LobbyClosedEvent event1 = new LobbyClosedEvent("lobby1");
        LobbyClosedEvent event2 = new LobbyClosedEvent("lobby1");
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void hashCodeReturnsDifferentValueForDifferentLobbyId() {
        LobbyClosedEvent event1 = new LobbyClosedEvent("lobby1");
        LobbyClosedEvent event2 = new LobbyClosedEvent("lobby2");
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }
}