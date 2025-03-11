package de.uol.swp.common.player.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrawInfectionCardRequestTest {

    @Test
    void testEquals() {
        DrawInfectionCardRequest request1 = new DrawInfectionCardRequest("lobby1");
        DrawInfectionCardRequest request2 = new DrawInfectionCardRequest("lobby1");
        DrawInfectionCardRequest request3 = new DrawInfectionCardRequest("lobby2");

        assertNotEquals(request1, request2, "Expected requests with the same lobby code to be not equal");
        assertNotEquals(request1, request3, "Expected requests with different lobby codes to be not equal");
    }

    @Test
    void testHashCode() {
        DrawInfectionCardRequest request1 = new DrawInfectionCardRequest("lobby1");
        DrawInfectionCardRequest request2 = new DrawInfectionCardRequest("lobby1");

        assertEquals(0, request1.hashCode(), "Expected hashCode to be 0");
        assertEquals(0, request2.hashCode(), "Expected hashCode to be 0");
    }
}