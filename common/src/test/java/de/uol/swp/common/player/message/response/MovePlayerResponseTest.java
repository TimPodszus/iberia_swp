package de.uol.swp.common.player.message.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovePlayerResponseTest {

    @Test
    void testConstructorAndGetters() {
        String lobbyId = "testLobby";
        boolean success = true;
        String description = "Player moved successfully";

        MovePlayerResponse response = new MovePlayerResponse(lobbyId, success, description);

        assertEquals(lobbyId, response.getLobbyId());
        assertEquals(success, response.isSuccess());
    }

    @Test
    void testEquals() {
        String lobbyId = "testLobby";
        boolean success = true;
        String description = "Player moved successfully";

        MovePlayerResponse response1 = new MovePlayerResponse(lobbyId, success, description);
        MovePlayerResponse response2 = new MovePlayerResponse(lobbyId, success, description);
        MovePlayerResponse response3 = new MovePlayerResponse("differentLobby", success, description);
        MovePlayerResponse response4 = new MovePlayerResponse(lobbyId, false, description);

        // Test equality with itself
        assertEquals(response1, response1);

        // Test equality with another object with the same values
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());

        // Test inequality with null
        assertNotEquals(response1, null);

        // Test inequality with an object of a different class
        assertNotEquals(response1, new Object());

        // Test inequality with different lobbyId
        assertNotEquals(response1, response3);

        // Test inequality with different success value
        assertNotEquals(response1, response4);
    }
}