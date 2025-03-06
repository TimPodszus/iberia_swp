package de.uol.swp.common.player.message.response;

import de.uol.swp.common.cards.data.ICardDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class DrawPlayerCardResponseTest {

    @Test
    void testConstructorAndGetters() {
        String lobbyCode = "testLobby";
        boolean success = true;
        String description = "Card drawn successfully";
        ICardDTO card = mock(ICardDTO.class);

        DrawPlayerCardResponse response = new DrawPlayerCardResponse(lobbyCode, success, description, card);

        assertEquals(lobbyCode, response.getLobbyId());
        assertEquals(success, response.isSuccess());
        assertEquals(description, response.getDescription());
    }

    @Test
    void testEquals() {
        String lobbyCode = "testLobby";
        boolean success = true;
        String description = "Card drawn successfully";
        ICardDTO card = mock(ICardDTO.class);

        DrawPlayerCardResponse response1 = new DrawPlayerCardResponse(lobbyCode, success, description, card);
        DrawPlayerCardResponse response2 = new DrawPlayerCardResponse(lobbyCode, success, description, card);
        DrawPlayerCardResponse response3 = new DrawPlayerCardResponse("differentLobby", success, description, card);
        DrawPlayerCardResponse response4 = new DrawPlayerCardResponse(lobbyCode, false, description, card);

        // Test equality with itself
        assertEquals(response1, response1);

        // Test equality with another object with the same values
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());

        // Test inequality with null
        assertNotEquals(response1, null);

        // Test inequality with an object of a different class
        assertNotEquals(response1, new Object());

        // Test inequality with different lobbyCode
        assertNotEquals(response1, response3);

        // Test inequality with different success value
        assertNotEquals(response1, response4);
    }
}