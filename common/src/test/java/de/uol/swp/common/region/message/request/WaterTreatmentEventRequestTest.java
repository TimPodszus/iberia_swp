package de.uol.swp.common.region.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaterTreatmentEventRequestTest {

    @Test
    void testEqualsAndHashCode() {
        WaterTreatmentEventRequest request1 = new WaterTreatmentEventRequest("lobby1", 1, 100, true);
        WaterTreatmentEventRequest request2 = new WaterTreatmentEventRequest("lobby1", 1, 100, true);
        WaterTreatmentEventRequest request3 = new WaterTreatmentEventRequest("lobby2", 2, 200, false);

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testGetters() {
        WaterTreatmentEventRequest request = new WaterTreatmentEventRequest("lobby1", 1, 100, true);

        assertEquals("lobby1", request.getLobbyId());
        assertEquals(1, request.getRegionId());
        assertEquals(100, request.getAmount());
        assertTrue(request.isDismissed());
    }
}