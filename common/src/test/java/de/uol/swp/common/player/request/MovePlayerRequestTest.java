package de.uol.swp.common.player.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MovePlayerRequest class.
 */
public class MovePlayerRequestTest {
    private static final String LOBBY_CODE = "testLobbyCode";
    private static final int CITY_ID = 1;
    private static final int CARD_ID = 1;

    /**
     * Tests the creation of a MovePlayerRequest and verifies its fields.
     */
    @Test
    void testRequest() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest(LOBBY_CODE, CITY_ID, CARD_ID);
        assertEquals(LOBBY_CODE, movePlayerRequest.getLobbyId());
        assertEquals(CITY_ID, movePlayerRequest.getCityId());
    }

    /**
     * Tests the equals method for two identical MovePlayerRequest objects.
     */
    @Test
    void testEquals() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest(LOBBY_CODE, CITY_ID, CARD_ID);
        MovePlayerRequest movePlayerRequest1 = new MovePlayerRequest(LOBBY_CODE, CITY_ID, CARD_ID);
        assertEquals(movePlayerRequest, movePlayerRequest1);
    }

    /**
     * Tests the equals method for the same MovePlayerRequest object.
     */
    @Test
    void testEqualsWithSameObject() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest(LOBBY_CODE, CITY_ID, CARD_ID);
        assertEquals(movePlayerRequest, movePlayerRequest);
    }

    /**
     * Tests the equals method with an object of a different class.
     */
    @Test
    void testEqualsWithOtherClass() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest(LOBBY_CODE, CITY_ID, CARD_ID);
        assertNotEquals(movePlayerRequest, new Object());
    }

    /**
     * Tests the hashCode method for two identical MovePlayerRequest objects.
     */
    @Test
    void testHashCode() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest(LOBBY_CODE, CITY_ID, CARD_ID);
        MovePlayerRequest movePlayerRequest1 = new MovePlayerRequest(LOBBY_CODE, CITY_ID, CARD_ID);
        assertEquals(movePlayerRequest.hashCode(), movePlayerRequest1.hashCode());
    }
}
