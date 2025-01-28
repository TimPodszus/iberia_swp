package de.uol.swp.common.player.request;

import de.uol.swp.common.cards.CityCardDTO;
import de.uol.swp.common.cards.ICardDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MovePlayerRequest class.
 */
public class MovePlayerRequestTest {
    private static final String LOBBY_CODE = "testLobbyCode";
    private static final int CITY_ID = 1;
    private static final ICardDTO CARD = new CityCardDTO(1, "TestCity", null);

    /**
     * Tests the creation of a MovePlayerRequest and verifies its fields.
     */
    @Test
    void testRequest() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest(LOBBY_CODE, CITY_ID, CARD);
        assertEquals(LOBBY_CODE, movePlayerRequest.getLobbyId());
        assertEquals(CITY_ID, movePlayerRequest.getCityId());
    }

    /**
     * Tests the equals method for two identical MovePlayerRequest objects.
     */
    @Test
    void testEquals() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest(LOBBY_CODE, CITY_ID, CARD);
        MovePlayerRequest movePlayerRequest1 = new MovePlayerRequest(LOBBY_CODE, CITY_ID, CARD);
        assertEquals(movePlayerRequest, movePlayerRequest1);
    }

    /**
     * Tests the equals method for the same MovePlayerRequest object.
     */
    @Test
    void testEqualsWithSameObject() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest(LOBBY_CODE, CITY_ID, CARD);
        assertEquals(movePlayerRequest, movePlayerRequest);
    }

    /**
     * Tests the equals method with an object of a different class.
     */
    @Test
    void testEqualsWithOtherClass() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest(LOBBY_CODE, CITY_ID, CARD);
        assertNotEquals(movePlayerRequest, new Object());
    }

    /**
     * Tests the hashCode method for two identical MovePlayerRequest objects.
     */
    @Test
    void testHashCode() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest(LOBBY_CODE, CITY_ID, CARD);
        MovePlayerRequest movePlayerRequest1 = new MovePlayerRequest(LOBBY_CODE, CITY_ID, CARD);
        assertEquals(movePlayerRequest.hashCode(), movePlayerRequest1.hashCode());
    }
}
