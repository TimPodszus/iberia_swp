package de.uol.swp.common.game.message.event;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.PlagueName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test class for ShareRideEvent.
 */
public class ShareRideEventTest {

    private static final String LOBBY_ID = "lobbyId";
    private static final ICityDTO TEST_CITY = new CityDTO(1,
            PlagueName.CHOLERA,
            CityName.MADRID,
            1010,
            false,
            false,
            new ArrayList<>()
    );

    /**
     * Tests the constructor of ShareRideEvent.
     */
    @Test
    void testConstructor() {
        ShareRideEvent event = new ShareRideEvent(LOBBY_ID, TEST_CITY);

        assertEquals(LOBBY_ID, event.getLobbyId(), "The lobby ID is not set correctly.");
        assertEquals(TEST_CITY, event.getCity(), "The city is not set correctly.");
    }

    /**
     * Tests the equals method of ShareRideEvent with two identical events.
     */
    @Test
    void testEquals() {
        ShareRideEvent event1 = new ShareRideEvent(LOBBY_ID, TEST_CITY);
        ShareRideEvent event2 = new ShareRideEvent(LOBBY_ID, TEST_CITY);

        assertEquals(event1, event2, "The two ShareRideEvent should be equal.");
    }

    /**
     * Tests the equals method of ShareRideEvent with the same object.
     */
    @Test
    void testEqualsWithSameObject() {
        ShareRideEvent event = new ShareRideEvent(LOBBY_ID, TEST_CITY);

        assertEquals(event, event, "The ShareRideEvent should be equal to itself.");
    }

    /**
     * Tests the equals method of ShareRideEvent with a different object.
     */
    @Test
    void testEqualsWithDifferentObject() {
        ShareRideEvent event = new ShareRideEvent(LOBBY_ID, TEST_CITY);
        Object object = new Object();

        assertNotEquals(event, object, "The ShareRideEvent should not be equal to an object of a different type.");
    }

    /**
     * Tests the hashCode method of ShareRideEvent.
     */
    @Test
    void testHashCode() {
        ShareRideEvent event = new ShareRideEvent(LOBBY_ID, TEST_CITY);

        assertEquals(event.hashCode(), event.hashCode(), "The hash code should be consistent.");
    }
}