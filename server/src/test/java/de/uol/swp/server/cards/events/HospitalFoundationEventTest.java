package de.uol.swp.server.cards.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class HospitalFoundationEventTest {
    private static final String LOBBY_ID = "LobbyID";
    private static final String USERNAME = "username";

    @Test
    void testConstructor() {
        HospitalFoundationEvent hospitalFoundationEvent = new HospitalFoundationEvent(LOBBY_ID, USERNAME);

        assertEquals(LOBBY_ID, hospitalFoundationEvent.getLobbyId());
        assertEquals(USERNAME, hospitalFoundationEvent.getUsername());
    }

    @Test
    void testEquals() {
        HospitalFoundationEvent hospitalFoundationEvent1 = new HospitalFoundationEvent(LOBBY_ID, USERNAME);
        HospitalFoundationEvent hospitalFoundationEvent2 = new HospitalFoundationEvent(LOBBY_ID, USERNAME);

        assertEquals(hospitalFoundationEvent1, hospitalFoundationEvent2);
    }

    @Test
    void testEqualsWithSameObject() {
        HospitalFoundationEvent hospitalFoundationEvent = new HospitalFoundationEvent(LOBBY_ID, USERNAME);

        assertEquals(hospitalFoundationEvent, hospitalFoundationEvent);
    }

    @Test
    void testEqualsWithWrongObject() {
        HospitalFoundationEvent hospitalFoundationEvent = new HospitalFoundationEvent(LOBBY_ID, USERNAME);

        assertNotEquals(hospitalFoundationEvent, new Object());
    }

    @Test
    void testHashCode() {
        HospitalFoundationEvent hospitalFoundationEvent = new HospitalFoundationEvent(LOBBY_ID, USERNAME);

        assertEquals(hospitalFoundationEvent.hashCode(), hospitalFoundationEvent.hashCode());
    }
}
