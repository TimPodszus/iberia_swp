package de.uol.swp.common.region.message.response;

import de.uol.swp.common.message.response.AbstractResponseMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TreatWaterEventResponseTest {

    @Test
    void testEqualsAndHashCode() {
        TreatWaterEventResponse response1 = new TreatWaterEventResponse("lobbyId", true);
        TreatWaterEventResponse response2 = new TreatWaterEventResponse("lobbyId", true);
        TreatWaterEventResponse response3 = new TreatWaterEventResponse("lobbyId", false);

        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    void testConstructor() {
        TreatWaterEventResponse response = new TreatWaterEventResponse("lobbyId", true);
        assertNotNull(response);
        assertTrue(response.isDismissible());

        response = new TreatWaterEventResponse("lobbyId", false);
        assertNotNull(response);
        assertFalse(response.isDismissible());
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        TreatWaterEventResponse response = new TreatWaterEventResponse("lobbyId", true);
        assertNotEquals(null, response);
        assertNotEquals(new Object(), response);
    }

    @Test
    void testEqualsWithDifferentSubclass() {
        TreatWaterEventResponse response = new TreatWaterEventResponse("lobbyId", true);
        AbstractResponseMessage differentSubclass = new AbstractResponseMessage() {};
        assertNotEquals(response, differentSubclass);
    }
}