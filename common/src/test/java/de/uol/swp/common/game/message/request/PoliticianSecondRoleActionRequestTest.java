package de.uol.swp.common.game.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PoliticianSecondRoleActionRequestTest {

    @Test
    void testEquals() {
        PoliticianSecondRoleActionRequest request1 = new PoliticianSecondRoleActionRequest("lobby1");
        PoliticianSecondRoleActionRequest request2 = new PoliticianSecondRoleActionRequest("lobby1");
        PoliticianSecondRoleActionRequest request3 = new PoliticianSecondRoleActionRequest("lobby2");

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
    }

    @Test
    void testHashCode() {
        PoliticianSecondRoleActionRequest request1 = new PoliticianSecondRoleActionRequest("lobby1");
        PoliticianSecondRoleActionRequest request2 = new PoliticianSecondRoleActionRequest("lobby1");
        PoliticianSecondRoleActionRequest request3 = new PoliticianSecondRoleActionRequest("lobby2");

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
}