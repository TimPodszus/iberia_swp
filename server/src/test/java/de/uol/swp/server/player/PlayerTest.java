package de.uol.swp.server.player;

import de.uol.swp.common.user.User;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerTest {
    private Player player;
    private City mockCity;
    private CityCard mockCityCard;
    private List<Card> cards;

    @BeforeEach
    void setUp() {
        mockCity = mock(City.class);
        mockCityCard = mock(CityCard.class);
        when(mockCityCard.getCity()).thenReturn(mockCity);
        cards = new ArrayList<>();
        player = new Player(mock(User.class));
        player.setCards(cards);
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
}
