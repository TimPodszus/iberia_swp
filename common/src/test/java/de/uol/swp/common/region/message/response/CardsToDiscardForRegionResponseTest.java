package de.uol.swp.common.region.message.response;

import de.uol.swp.common.cards.data.CityCardDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CardsToDiscardForRegionResponseTest {

    @Test
    void testEquals() {
        CityCardDTO card1 = mock(CityCardDTO.class);
        CityCardDTO card2 = mock(CityCardDTO.class);

        List<CityCardDTO> cardsList1 = List.of(card1, card2);
        List<CityCardDTO> cardsList2 = List.of(card1, card2);
        List<CityCardDTO> cardsList3 = List.of(card1);

        CardsToDiscardForRegionResponse response1 = new CardsToDiscardForRegionResponse(cardsList1);
        CardsToDiscardForRegionResponse response2 = new CardsToDiscardForRegionResponse(cardsList2);
        CardsToDiscardForRegionResponse response3 = new CardsToDiscardForRegionResponse(cardsList3);

        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
    }

    @Test
    void testHashCode() {
        CityCardDTO card1 = mock(CityCardDTO.class);
        CityCardDTO card2 = mock(CityCardDTO.class);

        List<CityCardDTO> cardsList1 = List.of(card1, card2);
        List<CityCardDTO> cardsList2 = List.of(card1, card2);
        List<CityCardDTO> cardsList3 = List.of(card1);

        CardsToDiscardForRegionResponse response1 = new CardsToDiscardForRegionResponse(cardsList1);
        CardsToDiscardForRegionResponse response2 = new CardsToDiscardForRegionResponse(cardsList2);
        CardsToDiscardForRegionResponse response3 = new CardsToDiscardForRegionResponse(cardsList3);

        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }
}