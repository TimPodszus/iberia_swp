package de.uol.swp.server.player;

import de.uol.swp.common.user.User;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityName;
import de.uol.swp.server.city.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerTest {
    private Player player;
    private City mockCity;
    private CityCard mockCityCard;
    private List<Card> cards;
    private CityRepository mockCityRepository;

    @BeforeEach
    void setUp() {
        mockCity = mock(City.class);
        mockCityCard = mock(CityCard.class);
        mockCityRepository = mock(CityRepository.class);
        when(mockCityCard.getCity()).thenReturn(mockCity);
        cards = new ArrayList<>();
        player = new Player(mock(User.class));
        player.setCards(cards);
        player.cityRepository = mockCityRepository;
    }

    /*
    Auskommentiert, da die Methode nicht implementiert ist

    @Test
    void playCard_AddsCardToHand() {
        Card card = mock(Card.class);
        player.playCard(card);
        assertTrue(player.getCards().contains(card));
    }
*/
    @Test
    void discardCard_RemovesCardFromHand() {
        Card card = mock(Card.class);
        cards.add(card);
        player.discardCard(card);
        assertFalse(player.getCards()
                          .contains(card));
    }


    @Test
    void testSetStartingPosition() throws Exception {
        when(mockCity.getName()).thenReturn(CityName.BARCELONA);
        cards.add(mockCityCard);

        City mockCityFromRepo = mock(City.class);
        when(mockCityRepository.getCitiesByNames(CityName.BARCELONA)).thenReturn(Collections.singletonList(mockCityFromRepo));

        player.setStartingPosition("BARCELONA");

        assertEquals(mockCityFromRepo, player.getCurrentPosition());
    }

    @Test
    void testSetStartingPosition_WithInvalidCityCard() {
        when(mockCity.getName()).thenReturn(CityName.ALICANTE);
        cards.add(mockCityCard);

        assertThrows(Exception.class, () -> player.setStartingPosition("BARCELONA"));

    }
}
