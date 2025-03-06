package de.uol.swp.common.player.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DrawPlayerCardRequestTest {

    @Test
    void testConstructorAndGetters() {
        String lobbyCode = "testLobby";

        DrawPlayerCardRequest request = new DrawPlayerCardRequest(lobbyCode);

        assertEquals(lobbyCode, request.getLobbyId());
    }

    @Test
    void testEquals() {
        String lobbyCode = "testLobby";

        DrawPlayerCardRequest request1 = new DrawPlayerCardRequest(lobbyCode);
        DrawPlayerCardRequest request2 = new DrawPlayerCardRequest(lobbyCode);
        DrawPlayerCardRequest request3 = new DrawPlayerCardRequest("differentLobby");

        // Test equality with itself
        assertEquals(request1, request1);

        // Test equality with another object with the same values
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());

        // Test inequality with null
        assertNotEquals(request1, null);

        // Test inequality with an object of a different class
        assertNotEquals(request1, new Object());

        // Test inequality with different lobbyCode
        assertNotEquals(request1, request3);
    }
}