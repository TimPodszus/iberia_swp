package de.uol.swp.common.player.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrawPlayerCardRequestTest {

    @Test
    void testConstructorAndGetters() {
        DrawPlayerCardRequest request = new DrawPlayerCardRequest("lobby123");

        assertEquals("lobby123", request.getLobbyId());
    }

    @Test
    void testEqualsAndHashCode() {
        DrawPlayerCardRequest request1 = new DrawPlayerCardRequest("lobby123");
        DrawPlayerCardRequest request2 = new DrawPlayerCardRequest("lobby123");
        DrawPlayerCardRequest request3 = new DrawPlayerCardRequest("lobby456");

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testNotEqualsDifferentClass() {
        DrawPlayerCardRequest request = new DrawPlayerCardRequest("lobby123");
        Object other = new Object();

        assertNotEquals(request, other);
    }

    @Test
    void testNotEqualsNull() {
        DrawPlayerCardRequest request = new DrawPlayerCardRequest("lobby123");

        assertNotEquals(null, request);
    }

    @Test
    void testEqualsSameObject() {
        DrawPlayerCardRequest request = new DrawPlayerCardRequest("lobby123");

        assertEquals(request, request);
    }
}