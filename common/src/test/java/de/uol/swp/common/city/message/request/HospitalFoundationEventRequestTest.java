package de.uol.swp.common.city.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class HospitalFoundationEventRequestTest {
    private static final String LOBBY_ID = "LobbyID";
    private static final Integer CITY_ID = 1;

    @Test
    void testConstructor() {
        HospitalFoundationEventRequest request = new HospitalFoundationEventRequest(LOBBY_ID, CITY_ID);

        assertEquals(LOBBY_ID, request.getLobbyId());
        assertEquals(CITY_ID, request.getCityId());
    }

    @Test
    void testEquals() {
        HospitalFoundationEventRequest request1 = new HospitalFoundationEventRequest(LOBBY_ID, CITY_ID);
        HospitalFoundationEventRequest request2 = new HospitalFoundationEventRequest(LOBBY_ID, CITY_ID);

        assertEquals(request1, request2);
    }

    @Test
    void testEqualsWithSameObject() {
        HospitalFoundationEventRequest request = new HospitalFoundationEventRequest(LOBBY_ID, CITY_ID);

        assertEquals(request, request);
    }

    @Test
    void testEqualsWithWrongObject() {
        HospitalFoundationEventRequest request = new HospitalFoundationEventRequest(LOBBY_ID, CITY_ID);

        assertNotEquals(request, new Object());
    }

    @Test
    void testHashCode() {
        HospitalFoundationEventRequest request = new HospitalFoundationEventRequest(LOBBY_ID, CITY_ID);

        assertEquals(request.hashCode(), request.hashCode());
    }
}