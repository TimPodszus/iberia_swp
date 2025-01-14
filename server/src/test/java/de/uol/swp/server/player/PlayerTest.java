package de.uol.swp.server.player;

import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.City;
import de.uol.swp.common.city.CityName;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.usermanagement.IUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for the {@link Player} class.
 * Ensures that the functionality of the Player class, such as managing cards and setting the starting position, works as expected.
 */
class PlayerTest {

    /**
     * The instance of {@link Player} being tested.
     */
    private Player player;

    /**
     * Mocked instance of {@link City} used for testing.
     */
    private City mockCity;

    /**
     * Mocked instance of {@link CityCard} used for testing.
     */
    private CityCard mockCityCard;

    /**
     * A list of {@link Card} objects representing the player's hand during tests.
     */
    private List<Card> cards;

    /**
     * Sets up the test environment by initializing mocks and the Player instance.
     */
    @BeforeEach
    void setUp() {
        mockCity = mock(City.class);
        mockCityCard = mock(CityCard.class);
        when(mockCityCard.getCity()).thenReturn(mockCity);
        cards = new ArrayList<>();
        player = new Player(mock(IUser.class));
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
    /**
     * Tests that the {@link Player#discardCard(Card)} method correctly removes a card from the player's hand.
     */
    @Test
    void discardCard_RemovesCardFromHand() {
        Card card = mock(Card.class);
        cards.add(card);
        player.discardCard(card);
        assertFalse(player.getCards()
                          .contains(card));
    }

    /**
     * Tests that the {@link Player#setStartingPosition(String)} method throws an exception
     * when the city name does not match any city card in the player's hand.
     */
    @Test
    void testSetStartingPosition_WithInvalidCityCard() {
        when(mockCity.getName()).thenReturn(CityName.ALICANTE);
        cards.add(mockCityCard);

        assertThrows(Exception.class, () -> player.setStartingPosition(CityName.BARCELONA));
    }
}
