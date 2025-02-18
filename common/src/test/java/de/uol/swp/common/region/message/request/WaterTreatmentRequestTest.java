package de.uol.swp.common.region.message.request;

import de.uol.swp.common.cards.data.CityCardDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class WaterTreatmentRequestTest {

    @Test
    void testEquals() {
        CityCardDTO card1 = mock(CityCardDTO.class);
        CityCardDTO card2 = mock(CityCardDTO.class);

        WaterTreatmentRequest request1 = new WaterTreatmentRequest("lobby1", 1, 100, card1);
        WaterTreatmentRequest request2 = new WaterTreatmentRequest("lobby1", 1, 100, card1);
        WaterTreatmentRequest request3 = new WaterTreatmentRequest("lobby1", 1, 200, card1);
        WaterTreatmentRequest request4 = new WaterTreatmentRequest("lobby1", 1, 100, card2);
        WaterTreatmentRequest request5 = new WaterTreatmentRequest("lobby2", 1, 100, card1);

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, request4);
        assertNotEquals(request1, request5);
    }

    @Test
    void testHashCode() {
        CityCardDTO card1 = mock(CityCardDTO.class);
        CityCardDTO card2 = mock(CityCardDTO.class);

        WaterTreatmentRequest request1 = new WaterTreatmentRequest("lobby1", 1, 100, card1);
        WaterTreatmentRequest request2 = new WaterTreatmentRequest("lobby1", 1, 100, card1);
        WaterTreatmentRequest request3 = new WaterTreatmentRequest("lobby1", 1, 200, card1);
        WaterTreatmentRequest request4 = new WaterTreatmentRequest("lobby1", 1, 100, card2);
        WaterTreatmentRequest request5 = new WaterTreatmentRequest("lobby2", 1, 100, card1);

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
        assertNotEquals(request1.hashCode(), request4.hashCode());
        assertNotEquals(request1.hashCode(), request5.hashCode());
    }
}