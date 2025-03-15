package de.uol.swp.common.game.message.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StatusResponseTest {
    private static final String LOBBY_ID = "LobbyCode";
    private static final String DESCRIPTION = "Description";

    /**
     * Tests the creation of a StatusResponse object.
     */
    @Test
    void testStatusResponse() {
        StatusResponse statusResponse = new StatusResponse(LOBBY_ID, true, DESCRIPTION);
        assertEquals(LOBBY_ID, statusResponse.getLobbyId());
    }

    /**
     * Tests the equality of two StatusResponse objects with the same values.
     */
    @Test
    void testEquals() {
        StatusResponse statusResponse = new StatusResponse(LOBBY_ID, true, DESCRIPTION);
        StatusResponse statusResponse1 = new StatusResponse(LOBBY_ID, true, DESCRIPTION);
        assertEquals(statusResponse, statusResponse1);
    }

    /**
     * Tests the equality of the same StatusResponse objects.
     */
    @Test
    void testEqualsWithSameObjects() {
        StatusResponse statusResponse = new StatusResponse(LOBBY_ID, true, DESCRIPTION);
        assertEquals(statusResponse, statusResponse);
    }

    /**
     * Tests the inequality of a StatusResponse object with a different type of object.
     */
    @Test
    void testEqualsWithDifferentObjects() {
        StatusResponse statusResponse = new StatusResponse(LOBBY_ID, true, DESCRIPTION);
        Object object = new Object();
        assertNotEquals(statusResponse, object);
    }

    /**
     * Tests the hashCode method of the StatusResponse object.
     */
    @Test
    void testHashCode() {
        StatusResponse statusResponse = new StatusResponse(LOBBY_ID, true, DESCRIPTION);
        assertEquals(statusResponse.hashCode(), statusResponse.hashCode());
    }
}
