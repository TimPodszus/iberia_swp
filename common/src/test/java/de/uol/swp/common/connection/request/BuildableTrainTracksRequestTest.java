package de.uol.swp.common.connection.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildableTrainTracksRequestTest {

    @Test
    void testConstructorAndGetter() {
        String lobbyId = "testLobby";
        int cityId = 42;
        BuildableTrainTracksRequest request = new BuildableTrainTracksRequest(lobbyId, cityId);

        assertEquals(lobbyId, request.getLobbyId());
        assertEquals(cityId, request.getCityId());
    }

    @Test
    void testEquals() {
        BuildableTrainTracksRequest request1 = new BuildableTrainTracksRequest("lobby1", 100);
        BuildableTrainTracksRequest request2 = new BuildableTrainTracksRequest("lobby1", 100);
        BuildableTrainTracksRequest request3 = new BuildableTrainTracksRequest("lobby1", 200);
        BuildableTrainTracksRequest request4 = new BuildableTrainTracksRequest("lobby2", 100);

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, request4);
        assertNotEquals(request3, request4);
    }

    @Test
    void testEqualsWithDifferentClass() {
        BuildableTrainTracksRequest request = new BuildableTrainTracksRequest("lobby1", 100);
        Object other = new Object();
        assertNotEquals(request, other);
    }

    @Test
    void testEqualsWithNull() {
        BuildableTrainTracksRequest request = new BuildableTrainTracksRequest("lobby1", 100);
        assertNotEquals(null, request);
    }

    @Test
    void testHashCode() {
        BuildableTrainTracksRequest request1 = new BuildableTrainTracksRequest("lobby1", 100);
        BuildableTrainTracksRequest request2 = new BuildableTrainTracksRequest("lobby1", 100);
        BuildableTrainTracksRequest request3 = new BuildableTrainTracksRequest("lobby1", 200);

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
}
