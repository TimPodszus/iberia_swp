package de.uol.swp.common.game.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildTrainTrackRequestTest {

    @Test
    void testConstructorAndGetter() {
        String lobbyCode = "testLobby";
        int connectionId = 42;

        BuildTrainTrackRequest request = new BuildTrainTrackRequest(lobbyCode, connectionId);

        assertEquals(lobbyCode, request.getLobbyId());
        assertEquals(connectionId, request.getConnectionId());
    }

    @Test
    void testEquals() {
        BuildTrainTrackRequest request1 = new BuildTrainTrackRequest("lobby1", 1);
        BuildTrainTrackRequest request2 = new BuildTrainTrackRequest("lobby1", 1);
        BuildTrainTrackRequest request3 = new BuildTrainTrackRequest("lobby1", 2);
        BuildTrainTrackRequest request4 = new BuildTrainTrackRequest("lobby2", 1);

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
        assertNotEquals(request1, request4);
        assertNotEquals(request1, null);
        assertNotEquals(request1, new Object());
    }

    @Test
    void testHashCode() {
        BuildTrainTrackRequest request1 = new BuildTrainTrackRequest("lobby1", 1);
        BuildTrainTrackRequest request2 = new BuildTrainTrackRequest("lobby1", 1);
        BuildTrainTrackRequest request3 = new BuildTrainTrackRequest("lobby1", 2);

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
}
