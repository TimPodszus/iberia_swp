package de.uol.swp.common.region.message.response;

import de.uol.swp.common.message.response.AbstractResponseMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreatWaterEventResponseTest {

    @Test
    void testEqualsAndHashCode() {
        TreatWaterEventResponse response1 = new TreatWaterEventResponse(true);
        TreatWaterEventResponse response2 = new TreatWaterEventResponse(true);
        TreatWaterEventResponse response3 = new TreatWaterEventResponse(false);

        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    void testIsDismissible() {
        TreatWaterEventResponse response = new TreatWaterEventResponse(true);
        assertTrue(response.isDismissible());

        response = new TreatWaterEventResponse(false);
        assertFalse(response.isDismissible());
    }

    @Test
    void testConstructor() {
        TreatWaterEventResponse response = new TreatWaterEventResponse(true);
        assertNotNull(response);
        assertTrue(response.isDismissible());

        response = new TreatWaterEventResponse(false);
        assertNotNull(response);
        assertFalse(response.isDismissible());
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        TreatWaterEventResponse response = new TreatWaterEventResponse(true);
        assertNotEquals(null, response);
        assertNotEquals(new Object(), response);
    }

    @Test
    void testEqualsWithDifferentSubclass() {
        TreatWaterEventResponse response = new TreatWaterEventResponse(true);
        AbstractResponseMessage differentSubclass = new AbstractResponseMessage() {};
        assertNotEquals(response, differentSubclass);
    }
}