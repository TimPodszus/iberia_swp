package de.uol.swp.common.player.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for the MovePlayerRequest class.
 */
public class MovePlayerRequestTest {

    /**
     * Tests the creation of a MovePlayerRequest and verifies its fields.
     */
    @Test
    void testRequest() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("testLobbyCode", "testPlayerId", "testCityId");
        assertEquals("testLobbyCode", movePlayerRequest.getLobbyCode());
        assertEquals("testPlayerId", movePlayerRequest.getPlayerId());
        assertEquals("testCityId", movePlayerRequest.getCityId());
    }

    /**
     * Tests the equals method for two identical MovePlayerRequest objects.
     */
    @Test
    void testEquals() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("testLobbyCode", "testPlayerId", "testCityId");
        MovePlayerRequest movePlayerRequest1 = new MovePlayerRequest("testLobbyCode", "testPlayerId", "testCityId");
        assertEquals(movePlayerRequest, movePlayerRequest1);
    }

    /**
     * Tests the equals method for the same MovePlayerRequest object.
     */
    @Test
    void testEqualsWithSameObject() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("testLobbyCode", "testPlayerId", "testCityId");
        assertEquals(movePlayerRequest, movePlayerRequest);
    }

    /**
     * Tests the equals method with an object of a different class.
     */
    @Test
    void testEqualsWithOtherClass() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("testLobbyCode", "testPlayerId", "testCityId");
        boolean equals = movePlayerRequest.equals("testCityId");
        assertFalse(equals);
    }

    /**
     * Tests the hashCode method for two identical MovePlayerRequest objects.
     */
    @Test
    void testHashCode() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("testLobbyCode", "testPlayerId", "testCityId");
        MovePlayerRequest movePlayerRequest1 = new MovePlayerRequest("testLobbyCode", "testPlayerId", "testCityId");
        assertEquals(movePlayerRequest.hashCode(), movePlayerRequest1.hashCode());
    }
}
