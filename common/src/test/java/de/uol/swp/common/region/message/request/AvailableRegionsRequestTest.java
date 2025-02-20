package de.uol.swp.common.region.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AvailableRegionsRequestTest {

    @Test
    void testEquals() {
        AvailableRegionsRequest request1 = new AvailableRegionsRequest("lobby1");
        AvailableRegionsRequest request2 = new AvailableRegionsRequest("lobby1");
        AvailableRegionsRequest request3 = new AvailableRegionsRequest("lobby2");

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
    }

    @Test
    void testHashCode() {
        AvailableRegionsRequest request1 = new AvailableRegionsRequest("lobby1");
        AvailableRegionsRequest request2 = new AvailableRegionsRequest("lobby1");
        AvailableRegionsRequest request3 = new AvailableRegionsRequest("lobby2");

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
}