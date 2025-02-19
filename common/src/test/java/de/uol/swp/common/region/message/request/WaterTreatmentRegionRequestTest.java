package de.uol.swp.common.region.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaterTreatmentRegionRequestTest {

    @Test
    void testEquals() {
        WaterTreatmentRegionRequest request1 = new WaterTreatmentRegionRequest("lobby1", 1);
        WaterTreatmentRegionRequest request2 = new WaterTreatmentRegionRequest("lobby1", 1);
        WaterTreatmentRegionRequest request3 = new WaterTreatmentRegionRequest("lobby1", 2);
        WaterTreatmentRegionRequest request4 = new WaterTreatmentRegionRequest("lobby2", 1);

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, request4);
    }

    @Test
    void testHashCode() {
        WaterTreatmentRegionRequest request1 = new WaterTreatmentRegionRequest("lobby1", 1);
        WaterTreatmentRegionRequest request2 = new WaterTreatmentRegionRequest("lobby1", 1);
        WaterTreatmentRegionRequest request3 = new WaterTreatmentRegionRequest("lobby1", 2);
        WaterTreatmentRegionRequest request4 = new WaterTreatmentRegionRequest("lobby2", 1);

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
        assertNotEquals(request1.hashCode(), request4.hashCode());
    }
}