package de.uol.swp.common.player.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetCardsToSortRequestTest {

    @Test
    void testConstructorAndGetters() {
        GetCardsToSortRequest request = new GetCardsToSortRequest("lobby123");

        assertEquals("lobby123", request.getLobbyId());
    }

    @Test
    void testEqualsAndHashCode() {
        GetCardsToSortRequest request1 = new GetCardsToSortRequest("lobby123");
        GetCardsToSortRequest request2 = new GetCardsToSortRequest("lobby123");
        GetCardsToSortRequest request3 = new GetCardsToSortRequest("lobby456");

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testNotEqualsDifferentClass() {
        GetCardsToSortRequest request = new GetCardsToSortRequest("lobby123");
        Object other = new Object();

        assertNotEquals(request, other);
    }

    @Test
    void testNotEqualsNull() {
        GetCardsToSortRequest request = new GetCardsToSortRequest("lobby123");

        assertNotEquals(null, request);
    }

    @Test
    void testEqualsSameObject() {
        GetCardsToSortRequest request = new GetCardsToSortRequest("lobby123");

        assertEquals(request, request);
    }
}