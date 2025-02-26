package de.uol.swp.common.player.response;

import de.uol.swp.common.cards.data.ICardDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DrawPlayerCardResponseTest {

    @Test
    void testConstructorAndGetters() {
        ICardDTO card = mock(ICardDTO.class);
        DrawPlayerCardResponse response = new DrawPlayerCardResponse("lobby123", true, "description", card);

        assertEquals("lobby123", response.getLobbyId());
        assertTrue(response.isSuccess());
        assertEquals("description", response.getDescription());
        assertEquals(card, response.getCard());
    }

    @Test
    void testEqualsAndHashCode() {
        ICardDTO card1 = mock(ICardDTO.class);
        ICardDTO card2 = mock(ICardDTO.class);
        DrawPlayerCardResponse response1 = new DrawPlayerCardResponse("lobby123", true, "description", card1);
        DrawPlayerCardResponse response2 = new DrawPlayerCardResponse("lobby123", true, "description", card1);
        DrawPlayerCardResponse response3 = new DrawPlayerCardResponse("lobby123", true, "description", card2);

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, response3);
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    void testNotEqualsDifferentClass() {
        ICardDTO card = mock(ICardDTO.class);
        DrawPlayerCardResponse response = new DrawPlayerCardResponse("lobby123", true, "description", card);
        Object other = new Object();

        assertNotEquals(response, other);
    }

    @Test
    void testNotEqualsNull() {
        ICardDTO card = mock(ICardDTO.class);
        DrawPlayerCardResponse response = new DrawPlayerCardResponse("lobby123", true, "description", card);

        assertNotEquals(null, response);
    }

    @Test
    void testEqualsSameObject() {
        ICardDTO card = mock(ICardDTO.class);
        DrawPlayerCardResponse response = new DrawPlayerCardResponse("lobby123", true, "description", card);

        assertEquals(response, response);
    }
}