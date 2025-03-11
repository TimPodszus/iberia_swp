package de.uol.swp.common.player.message.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DiscardPlayerCardResponseTest {

    @Test
    void testConstructorAndGetters() {
        String lobbyId = "testLobby";
        boolean success = true;
        String description = "Card discarded successfully";

        DiscardPlayerCardResponse response = new DiscardPlayerCardResponse(lobbyId, success, description);

        assertEquals(lobbyId, response.getLobbyId());
        assertEquals(success, response.isSuccess());
        assertEquals(description, response.getDescription());
    }

    @Test
    void testEquals() {
        String lobbyId = "testLobby";
        boolean success = true;
        String description = "Card discarded successfully";

        DiscardPlayerCardResponse response1 = new DiscardPlayerCardResponse(lobbyId, success, description);
        DiscardPlayerCardResponse response2 = new DiscardPlayerCardResponse(lobbyId, success, description);
        DiscardPlayerCardResponse response3 = new DiscardPlayerCardResponse("differentLobby", success, description);
        DiscardPlayerCardResponse response4 = new DiscardPlayerCardResponse(lobbyId, false, description);

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