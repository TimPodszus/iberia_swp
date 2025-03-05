package de.uol.swp.common.game.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndTurnRequestTest {

    @Test
    void equals_sameObject() {
        EndTurnRequest request = new EndTurnRequest("lobby1");
        assertTrue(request.equals(request));
    }

    @Test
    void equals_nullObject() {
        EndTurnRequest request = new EndTurnRequest("lobby1");
        assertFalse(request.equals(null));
    }

    @Test
    void equals_differentClass() {
        EndTurnRequest request = new EndTurnRequest("lobby1");
        assertFalse(request.equals("someString"));
    }

    @Test
    void equals_differentLobbyId() {
        EndTurnRequest request1 = new EndTurnRequest("lobby1");
        EndTurnRequest request2 = new EndTurnRequest("lobby2");
        assertFalse(request1.equals(request2));
    }

    @Test
    void equals_sameLobbyId() {
        EndTurnRequest request1 = new EndTurnRequest("lobby1");
        EndTurnRequest request2 = new EndTurnRequest("lobby1");
        assertTrue(request1.equals(request2));
    }

    @Test
    void hashCode_sameLobbyId() {
        EndTurnRequest request1 = new EndTurnRequest("lobby1");
        EndTurnRequest request2 = new EndTurnRequest("lobby1");
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void hashCode_differentLobbyId() {
        EndTurnRequest request1 = new EndTurnRequest("lobby1");
        EndTurnRequest request2 = new EndTurnRequest("lobby2");
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }
}