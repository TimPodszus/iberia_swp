package de.uol.swp.common.game.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ShareRideRequest class.
 */
public class ShareRideRequestTest {

    private static final String LOBBY_ID = "LobbyCode";
    private static final int CITY_ID = 1;

    /**
     * Tests the constructor of ShareRideRequest.
     */
    @Test
    void testConstructorWithCityId() {
        ShareRideRequest shareRideRequest = new ShareRideRequest(LOBBY_ID, CITY_ID);

        assertEquals(LOBBY_ID, shareRideRequest.getLobbyId(), "The lobby ID is not set correctly.");
        assertTrue(shareRideRequest.isConfirmed(), "The request should be confirmed.");
        assertEquals(CITY_ID, shareRideRequest.getCityId(), "The city ID is not set correctly.");
    }

    @Test
    void testConstructorWithoutCityId() {
        ShareRideRequest shareRideRequest = new ShareRideRequest(LOBBY_ID);

        assertEquals(LOBBY_ID, shareRideRequest.getLobbyId(), "The lobby ID is not set correctly.");
        assertFalse(shareRideRequest.isConfirmed(), "The request should not be confirmed.");
        assertEquals(-1, shareRideRequest.getCityId(), "The city ID is not set correctly.");
    }

    /**
     * Tests the equals method with two equal ShareRideRequest objects.
     */
    @Test
    void testEquals() {
        ShareRideRequest shareRideRequest = new ShareRideRequest(LOBBY_ID, CITY_ID);
        ShareRideRequest shareRideRequest1 = new ShareRideRequest(LOBBY_ID, CITY_ID);

        assertEquals(shareRideRequest, shareRideRequest1, "The two ShareRideRequest should be equal.");
    }

    /**
     * Tests the equals method with the same ShareRideRequest object.
     */
    @Test
    void testEqualsWithEqualObjects() {
        ShareRideRequest shareRideRequest = new ShareRideRequest(LOBBY_ID, CITY_ID);

        assertEquals(shareRideRequest, shareRideRequest, "The ShareRideRequest should be equal to itself.");
    }

    /**
     * Tests the equals method with a ShareRideRequest object and a different object.
     */
    @Test
    void testEqualsWithDifferentObjects() {
        ShareRideRequest shareRideRequest = new ShareRideRequest(LOBBY_ID, CITY_ID);
        Object object = new Object();

        assertNotEquals(shareRideRequest,
                object,
                "The ShareRideRequest should not be equal to an object of a different type."
        );
    }

    /**
     * Tests the hashCode method of ShareRideRequest.
     */
    @Test
    void testHashCode() {
        ShareRideRequest shareRideRequest = new ShareRideRequest(LOBBY_ID, CITY_ID);

        assertEquals(shareRideRequest.hashCode(), shareRideRequest.hashCode(), "The hash code should be consistent.");
    }
}
