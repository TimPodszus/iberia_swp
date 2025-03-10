package de.uol.swp.common.plague.message.request;

import de.uol.swp.common.game.PlagueName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TreatPlagueRequestTest {

    @Test
    void testConstructorAndGetters() {
        PlagueName plague = PlagueName.CHOLERA;
        TreatPlagueRequest request = new TreatPlagueRequest("lobby123", 42, plague);

        assertEquals("lobby123", request.getLobbyId());
        assertEquals(42, request.getCityId());
        assertEquals(plague, request.getPlagueName());
    }

    @Test
    void testEquals() {
        PlagueName plague1 = PlagueName.CHOLERA;
        PlagueName plague2 = PlagueName.MALARIA;

        TreatPlagueRequest request1 = new TreatPlagueRequest("lobby123", 42, plague1);
        TreatPlagueRequest request2 = new TreatPlagueRequest("lobby123", 42, plague1);
        TreatPlagueRequest request3 = new TreatPlagueRequest("lobby456", 42, plague1);
        TreatPlagueRequest request4 = new TreatPlagueRequest("lobby123", 99, plague1);
        TreatPlagueRequest request5 = new TreatPlagueRequest("lobby123", 42, plague2);

        assertEquals(request1, request1);
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, request4);
        assertNotEquals(request1, request5);
        assertNotEquals(null, request1);
        assertNotEquals(new Object(), request1);
    }

    @Test
    void testHashCode() {
        PlagueName plague = PlagueName.CHOLERA;

        TreatPlagueRequest request1 = new TreatPlagueRequest("lobby123", 42, plague);
        TreatPlagueRequest request2 = new TreatPlagueRequest("lobby123", 42, plague);
        TreatPlagueRequest request3 = new TreatPlagueRequest("lobby456", 42, plague);

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
}