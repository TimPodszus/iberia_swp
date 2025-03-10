package de.uol.swp.common.cards.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test class for GetCardRequest.
 */
public class GetCardRequestTest {

    /**
     * Tests the constructor of GetCardRequest.
     */
    @Test
    void testConstructor() {
        GetCardRequest request = new GetCardRequest("lobbyId", 1);
        assertEquals("lobbyId", request.getLobbyId());
        assertEquals(1, request.getCardId());
    }

    /**
     * Tests the equals method of GetCardRequest with two identical objects.
     */
    @Test
    void testEquals() {
        GetCardRequest request1 = new GetCardRequest("lobbyId",1);
        GetCardRequest request2 = new GetCardRequest("lobbyId",1);

        assertEquals(request1, request2);
    }

    /**
     * Tests the equals method of GetCardRequest with the same object.
     */
    @Test
    void testEqualsWithSameObject() {
        GetCardRequest request = new GetCardRequest("lobbyId",1);
        assertEquals(request, request);
    }

    /**
     * Tests the equals method of GetCardRequest with a different object.
     */
    @Test
    void testEqualsWithDifferentObject() {
        GetCardRequest request = new GetCardRequest("lobbyId",1);
        assertNotEquals(request, new Object());
    }

    /**
     * Tests the hashCode method of GetCardRequest.
     */
    @Test
    void testHashCode() {
        GetCardRequest request = new GetCardRequest("lobbyId",1);
        assertEquals(1873456721, request.hashCode());
    }
}