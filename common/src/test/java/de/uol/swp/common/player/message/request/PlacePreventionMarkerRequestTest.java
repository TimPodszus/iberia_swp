package de.uol.swp.common.player.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlacePreventionMarkerRequestTest {

    @Test
    void testEqualsAndHashCode() {
        PlacePreventionMarkerRequest request1 = new PlacePreventionMarkerRequest("lobby1", 1);
        PlacePreventionMarkerRequest request2 = new PlacePreventionMarkerRequest("lobby1", 1);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testNotEquals() {
        PlacePreventionMarkerRequest request1 = new PlacePreventionMarkerRequest("lobby1", 1);
        PlacePreventionMarkerRequest request2 = new PlacePreventionMarkerRequest("lobby1", 2);
        PlacePreventionMarkerRequest request3 = new PlacePreventionMarkerRequest("lobby2", 1);

        assertNotEquals(request1, request2);
        assertNotEquals(request1, request3);
    }
    @Test
    void testEqualsWithSameObject() {
        PlacePreventionMarkerRequest request = new PlacePreventionMarkerRequest("lobby1", 1);
        assertEquals(request, request);
    }

    @Test
    void testEqualsWithNull() {
        PlacePreventionMarkerRequest request = new PlacePreventionMarkerRequest("lobby1", 1);
        assertNotEquals(null, request);
    }

    @Test
    void testEqualsWithDifferentClass() {
        PlacePreventionMarkerRequest request = new PlacePreventionMarkerRequest("lobby1", 1);
        String differentClassObject = "I am a string";
        assertNotEquals(request, differentClassObject);
    }
}