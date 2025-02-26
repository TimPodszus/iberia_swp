package de.uol.swp.common.player.response;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.player.message.response.CardsToSortResponse;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CardsToSortResponseTest {

    @Test
    void testConstructorAndGetters() {
        List<ICardDTO> cards = Arrays.asList(mock(ICardDTO.class), mock(ICardDTO.class));
        CardsToSortResponse response = new CardsToSortResponse("lobby123", true, "description", cards);

        assertEquals("lobby123", response.getLobbyId());
        assertTrue(response.isSuccess());
        assertEquals("description", response.getDescription());
        assertEquals(cards, response.getCards());
    }

    @Test
    void testEqualsAndHashCode() {
        List<ICardDTO> cards1 = Arrays.asList(mock(ICardDTO.class), mock(ICardDTO.class));
        List<ICardDTO> cards2 = Arrays.asList(mock(ICardDTO.class), mock(ICardDTO.class));
        CardsToSortResponse response1 = new CardsToSortResponse("lobby123", true, "description", cards1);
        CardsToSortResponse response2 = new CardsToSortResponse("lobby123", true, "description", cards1);
        CardsToSortResponse response3 = new CardsToSortResponse("lobby123", true, "description", cards2);

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, response3);
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    void testNotEqualsDifferentClass() {
        List<ICardDTO> cards = Arrays.asList(mock(ICardDTO.class), mock(ICardDTO.class));
        CardsToSortResponse response = new CardsToSortResponse("lobby123", true, "description", cards);
        Object other = new Object();

        assertNotEquals(response, other);
    }

    @Test
    void testNotEqualsNull() {
        List<ICardDTO> cards = Arrays.asList(mock(ICardDTO.class), mock(ICardDTO.class));
        CardsToSortResponse response = new CardsToSortResponse("lobby123", true, "description", cards);

        assertNotEquals(null, response);
    }
}