package de.uol.swp.server.Player;

import de.uol.swp.common.user.User;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    void setStartingPosition_WithCorrectCityCard_SetsPosition() throws Exception {
        cards.add(mockCityCard);
        player.setStartingPosition(mockCity);
        assertEquals(mockCity, player.getCurrentPosition());
    }

    @Test
    void setStartingPosition_WithNoMatchingCityCard_ThrowsException() {
        cards.add(mock(CityCard.class)); // A city card that does not match the target city
        Exception exception = assertThrows(Exception.class, () -> player.setStartingPosition(mockCity));
        assertEquals("Keine valide Stadt ausgewählt! Du musst eine Stadt die du auf der Hand hast auswählen!", exception.getMessage());
    }

    @Test
    void setStartingPosition_WithNoCityCards_AllowsSettingPosition() throws Exception {
        player.setStartingPosition(mockCity);
        assertEquals(mockCity, player.getCurrentPosition());
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
        assertFalse(player.getCards().contains(card));
    }
}
