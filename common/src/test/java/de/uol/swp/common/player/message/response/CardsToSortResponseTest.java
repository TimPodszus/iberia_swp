package de.uol.swp.common.player.message.response;

import de.uol.swp.common.cards.data.ICardDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class CardsToSortResponseTest {

    @Test
    void testConstructorAndGetters() {
        List<ICardDTO> cards = List.of(mock(ICardDTO.class), mock(ICardDTO.class), mock(ICardDTO.class));
        CardsToSortResponse response = new CardsToSortResponse("lobby1", true, "description1", cards);

        assertEquals("lobby1", response.getLobbyId());
        assertTrue(response.isSuccess());
        assertEquals("description1", response.getDescription());
        assertEquals(cards, response.getCards());
    }

    @Test
    void testEquals() {
        List<ICardDTO> cards1 = List.of(mock(ICardDTO.class), mock(ICardDTO.class), mock(ICardDTO.class));
        List<ICardDTO> cards2 = List.of(mock(ICardDTO.class), mock(ICardDTO.class), mock(ICardDTO.class));

        CardsToSortResponse response1 = new CardsToSortResponse("lobby1", true, "description1", cards1);
        CardsToSortResponse response2 = new CardsToSortResponse("lobby1", true, "description1", cards1);
        CardsToSortResponse response3 = new CardsToSortResponse("lobby1", true, "description1", cards2);

        assertEquals(response1, response1);
        assertNotEquals(null, response1);
        assertNotEquals(new Object(), response1);
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
    }

    @Test
    void testHashCode() {
        List<ICardDTO> cards1 = List.of(mock(ICardDTO.class), mock(ICardDTO.class), mock(ICardDTO.class));
        List<ICardDTO> cards2 = List.of(mock(ICardDTO.class), mock(ICardDTO.class), mock(ICardDTO.class));

        CardsToSortResponse response1 = new CardsToSortResponse("lobby1", true, "description1", cards1);
        CardsToSortResponse response2 = new CardsToSortResponse("lobby1", true, "description1", cards1);
        CardsToSortResponse response3 = new CardsToSortResponse("lobby1", true, "description1", cards2);

        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }
}