package de.uol.swp.common.connection.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AvailableDestinationsRequest.
 */
public class AvailableDestinationsRequestTest {
    private static final String LOBBY_CODE = "1";
    private static final int CITY_ID = 1;

    /**
     * Tests the creation of AvailableDestinationsRequest and its getters.
     */
    @Test
    void testRequest() {
        AvailableDestinationsRequest availableDestinationsRequest = new AvailableDestinationsRequest(LOBBY_CODE,
                CITY_ID
        );
        assertEquals(LOBBY_CODE, availableDestinationsRequest.getLobbyId());
        assertEquals(CITY_ID, availableDestinationsRequest.getCityId());
    }

    /**
     * Tests the equals method for two identical AvailableDestinationsRequest objects.
     */
    @Test
    void testEquals() {
        AvailableDestinationsRequest availableDestinationsRequest = new AvailableDestinationsRequest(LOBBY_CODE,
                CITY_ID
        );
        AvailableDestinationsRequest availableDestinationsRequest1 = new AvailableDestinationsRequest(LOBBY_CODE,
                CITY_ID
        );
        assertEquals(availableDestinationsRequest, availableDestinationsRequest1);
    }

    /**
     * Tests the equals method for the same AvailableDestinationsRequest object.
     */
    @Test
    void testEqualsWithSameObject() {
        AvailableDestinationsRequest availableDestinationsRequest = new AvailableDestinationsRequest(LOBBY_CODE,
                CITY_ID
        );
        assertEquals(availableDestinationsRequest, availableDestinationsRequest);
    }

    /**
     * Tests the equals method with an object of a different class.
     */
    @Test
    void testEqualsWithOtherClass() {
        AvailableDestinationsRequest availableDestinationsRequest = new AvailableDestinationsRequest(LOBBY_CODE,
                CITY_ID
        );
        assertNotEquals(availableDestinationsRequest, new Object());
    }

    /**
     * Tests the hashCode method for two identical AvailableDestinationsRequest objects.
     */
    @Test
    void testHashCode() {
        AvailableDestinationsRequest availableDestinationsRequest = new AvailableDestinationsRequest(LOBBY_CODE,
                CITY_ID
        );
        AvailableDestinationsRequest availableDestinationsRequest1 = new AvailableDestinationsRequest(LOBBY_CODE,
                CITY_ID
        );
        assertEquals(availableDestinationsRequest.hashCode(), availableDestinationsRequest1.hashCode());
    }
}