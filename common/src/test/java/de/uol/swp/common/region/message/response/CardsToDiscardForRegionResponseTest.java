package de.uol.swp.common.region.message.response;

import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

        CardsToDiscardForRegionResponse response1 = new CardsToDiscardForRegionResponse("lobbyId", cardsList1);
        CardsToDiscardForRegionResponse response2 = new CardsToDiscardForRegionResponse("lobbyId", cardsList2);
        CardsToDiscardForRegionResponse response3 = new CardsToDiscardForRegionResponse("lobbyId", cardsList3);

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

        CardsToDiscardForRegionResponse response1 = new CardsToDiscardForRegionResponse("lobbyId", cardsList1);
        CardsToDiscardForRegionResponse response2 = new CardsToDiscardForRegionResponse("lobbyId", cardsList2);
        CardsToDiscardForRegionResponse response3 = new CardsToDiscardForRegionResponse("lobbyId", cardsList3);

        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    void testConstructorAndGetCityCards() {
        CityCardDTO card1 = mock(CityCardDTO.class);
        CityCardDTO card2 = mock(CityCardDTO.class);

        List<CityCardDTO> cardsList = List.of(card1, card2);

        CardsToDiscardForRegionResponse response = new CardsToDiscardForRegionResponse("lobbyId", cardsList);

        assertNotNull(response.getCityCards());
        assertEquals(2,
                response.getCityCards()
                        .size()
        );
        assertTrue(response.getCityCards()
                           .contains(card1));
        assertTrue(response.getCityCards()
                           .contains(card2));
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        List<CityCardDTO> cardsList = List.of();
        CardsToDiscardForRegionResponse response = new CardsToDiscardForRegionResponse("lobbyId", cardsList);
        assertNotEquals(null, response);
        assertNotEquals(new Object(), response);
    }

    @Test
    void testEqualsWithDifferentSubclass() {
        List<CityCardDTO> cardsList = List.of();
        CardsToDiscardForRegionResponse response = new CardsToDiscardForRegionResponse("lobbyId", cardsList);
        AbstractResponseMessage differentSubclass = new AbstractResponseMessage() {
        };
        assertNotEquals(response, differentSubclass);
    }

    /**
     * Tests the equals method to ensure it correctly returns false
     * when the superclass comparison fails.
     */
    @Test
    void testEqualsWithDifferentSuperClassData() {
        List<CityCardDTO> cityCards = List.of(Mockito.mock(CityCardDTO.class));

        CardsToDiscardForRegionResponse response1 = new CardsToDiscardForRegionResponse("lobby123", cityCards);
        CardsToDiscardForRegionResponse response2 = new CardsToDiscardForRegionResponse("lobby456", cityCards);

        assertNotEquals(response1, response2);
    }
}