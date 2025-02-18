package de.uol.swp.server.cards.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MovePlayerAnywhereEventTest {

    private static final String LOBBY_ID = "LobbyID";
    private static final String USERNAME = "username";

    @Test
    void testConstructor() {
        MovePlayerAnywhereEvent movePlayerAnywhereEvent = new MovePlayerAnywhereEvent(LOBBY_ID, USERNAME);

        assertEquals(LOBBY_ID, movePlayerAnywhereEvent.getLobbyId());
        assertEquals(USERNAME, movePlayerAnywhereEvent.getUsername());
    }

    @Test
    void testEquals() {
        MovePlayerAnywhereEvent movePlayerAnywhereEvent1 = new MovePlayerAnywhereEvent(LOBBY_ID, USERNAME);
        MovePlayerAnywhereEvent movePlayerAnywhereEvent2 = new MovePlayerAnywhereEvent(LOBBY_ID, USERNAME);

        assertEquals(movePlayerAnywhereEvent1, movePlayerAnywhereEvent2);
    }

    @Test
    void testEqualsWithSameObject(){
        MovePlayerAnywhereEvent movePlayerAnywhereEvent = new MovePlayerAnywhereEvent(LOBBY_ID, USERNAME);

        assertEquals(movePlayerAnywhereEvent, movePlayerAnywhereEvent);
    }

    @Test
    void testEqualsWithWrongObject() {
        MovePlayerAnywhereEvent movePlayerAnywhereEvent = new MovePlayerAnywhereEvent(LOBBY_ID, USERNAME);

        assertNotEquals(movePlayerAnywhereEvent, new Object());
    }

    @Test
    void testHashCode() {
        MovePlayerAnywhereEvent movePlayerAnywhereEvent = new MovePlayerAnywhereEvent(LOBBY_ID, USERNAME);

        assertEquals(movePlayerAnywhereEvent.hashCode(), movePlayerAnywhereEvent.hashCode());
    }
}
