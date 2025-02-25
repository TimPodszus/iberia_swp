package de.uol.swp.common.player.request;

import de.uol.swp.common.cards.data.CardDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SortedCardsRequestTest {

    @Test
    void testConstructorAndGetters() {
        List<CardDTO> cards = List.of(mock(CardDTO.class));
        SortedCardsRequest request = new SortedCardsRequest("lobby123", cards);

        assertEquals("lobby123", request.getLobbyId());
        assertEquals(cards, request.getCards());
    }

    @Test
    void testEqualsAndHashCode() {
        List<CardDTO> cards1 = List.of(mock(CardDTO.class));
        List<CardDTO> cards2 = List.of(mock(CardDTO.class));
        SortedCardsRequest request1 = new SortedCardsRequest("lobby123", cards1);
        SortedCardsRequest request2 = new SortedCardsRequest("lobby123", cards1);
        SortedCardsRequest request3 = new SortedCardsRequest("lobby123", cards2);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testNotEqualsDifferentClass() {
        List<CardDTO> cards = List.of(mock(CardDTO.class));
        SortedCardsRequest request = new SortedCardsRequest("lobby123", cards);
        Object other = new Object();

        assertNotEquals(request, other);
    }

    @Test
    void testNotEqualsNull() {
        List<CardDTO> cards = List.of(mock(CardDTO.class));
        SortedCardsRequest request = new SortedCardsRequest("lobby123", cards);

        assertNotEquals(null, request);
    }

    @Test
    void testEqualsSameObject() {
        List<CardDTO> cards = List.of(mock(CardDTO.class));
        SortedCardsRequest request = new SortedCardsRequest("lobby123", cards);

        assertEquals(request, request);
    }
}