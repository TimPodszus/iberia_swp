package de.uol.swp.common.player.message.request;

import de.uol.swp.common.cards.data.ICardDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class DiscardPlayerCardRequestTest {

    @Test
    void testConstructorAndGetters() {
        String lobbyId = "testLobby";
        ICardDTO card = mock(ICardDTO.class);

        DiscardPlayerCardRequest request = new DiscardPlayerCardRequest(lobbyId, card);

        assertEquals(lobbyId, request.getLobbyId());
        assertEquals(card, request.getCard());
    }

    @Test
    void testEquals() {
        String lobbyId = "testLobby";
        ICardDTO card = mock(ICardDTO.class);
        List<ICardDTO> cards = List.of(card);

        DiscardPlayerCardRequest request1 = new DiscardPlayerCardRequest(lobbyId, card);
        DiscardPlayerCardRequest request2 = new DiscardPlayerCardRequest(lobbyId, card);
        DiscardPlayerCardRequest request3 = new DiscardPlayerCardRequest("differentLobby", card);
        DiscardPlayerCardRequest request4 = new DiscardPlayerCardRequest(lobbyId, mock(ICardDTO.class));

        // Test equality with itself
        assertEquals(request1, request1);

        // Test equality with another object with the same values
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());

        // Test inequality with null
        assertNotEquals(request1, null);

        // Test inequality with an object of a different class
        assertNotEquals(request1, new Object());

        // Test inequality with different lobbyId
        assertNotEquals(request1, request3);

        // Test inequality with different cards
        assertNotEquals(request1, request4);
    }
}