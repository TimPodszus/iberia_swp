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
    private static final String USERNAME = "testUsername";

    /**
     * Tests the creation of a MovePlayerRequest and verifies its fields.
     */
    @Test
    void testConstructorWithoutCardId() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest(LOBBY_CODE, CITY_ID, USERNAME);
        assertEquals(LOBBY_CODE, movePlayerRequest.getLobbyId(), "The lobby code should be set correctly.");
        assertEquals(CITY_ID, movePlayerRequest.getCityId(), "The city ID should be set correctly.");
        assertEquals(-1, movePlayerRequest.getCardId(), "The card ID should be set to -1.");
        assertEquals(USERNAME, movePlayerRequest.getUsername(), "The username should be set correctly.");
    }

    @Test
    void testConstructorWithoutUsername() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest(LOBBY_CODE, CITY_ID, CARD_ID);
        assertEquals(LOBBY_CODE, movePlayerRequest.getLobbyId(), "The lobby code should be set correctly.");
        assertEquals(CITY_ID, movePlayerRequest.getCityId(), "The city ID should be set correctly.");
        assertEquals(CARD_ID, movePlayerRequest.getCardId(), "The card ID should be set correctly.");
        assertEquals("", movePlayerRequest.getUsername(), "The username should be set to an empty string.");
    }

    @Test
    void testConstructorWithoutUsernameAndCardId() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest(LOBBY_CODE, CITY_ID);
        assertEquals(LOBBY_CODE, movePlayerRequest.getLobbyId(), "The lobby code should be set correctly.");
        assertEquals(CITY_ID, movePlayerRequest.getCityId(), "The city ID should be set correctly.");
        assertEquals(-1, movePlayerRequest.getCardId(), "The card ID should be set to -1.");
        assertEquals("", movePlayerRequest.getUsername(), "The username should be set to an empty string.");
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
