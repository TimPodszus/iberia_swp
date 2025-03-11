package de.uol.swp.common.city.message.response;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class HospitalFoundationEventResponseTest {
    private static final String LOBBY_ID = "LobbyID";
    private static final List<Integer> CITY_IDS = Arrays.asList(1, 2, 3);

    @Test
    void testConstructor() {
        HospitalFoundationEventResponse response = new HospitalFoundationEventResponse(LOBBY_ID, CITY_IDS);

        assertEquals(LOBBY_ID, response.getLobbyId());
        assertEquals(CITY_IDS, response.getCityIds());
    }

    @Test
    void testEquals() {
        HospitalFoundationEventResponse response1 = new HospitalFoundationEventResponse(LOBBY_ID, CITY_IDS);
        HospitalFoundationEventResponse response2 = new HospitalFoundationEventResponse(LOBBY_ID, CITY_IDS);

        assertEquals(response1, response2);
    }

    @Test
    void testEqualsWithSameObject() {
        HospitalFoundationEventResponse response = new HospitalFoundationEventResponse(LOBBY_ID, CITY_IDS);

        assertEquals(response, response);
    }

    @Test
    void testEqualsWithWrongObject() {
        HospitalFoundationEventResponse response = new HospitalFoundationEventResponse(LOBBY_ID, CITY_IDS);

        assertNotEquals(response, new Object());
    }

    @Test
    void testHashCode() {
        HospitalFoundationEventResponse response = new HospitalFoundationEventResponse(LOBBY_ID, CITY_IDS);

        assertEquals(response.hashCode(), response.hashCode());
    }
}