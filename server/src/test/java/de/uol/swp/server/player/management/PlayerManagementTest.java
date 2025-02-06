package de.uol.swp.server.player.management;

import de.uol.swp.common.cards.ICardDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.cards.*;
import de.uol.swp.server.city.data.City;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

/**
 * Test class for PlayerManagement.
 */
class PlayerManagementTest {

    private final IGame game = new Game(1, "123");

    @Mock
    private Player player;
    @Mock
    private IUser user;
    @Mock
    private EpidemicCard epidemicCard;
    @Mock
    private IGameManagement gameManagement;
    @Mock
    private ICityManagement cityManagement;

    private IPlayerManagement playerManagement;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        GameStore.getInstance()
                 .addGame(game.getGameId(), game);
        playerManagement = new PlayerManagement(gameManagement, cityManagement);
    }

    /**
     * Tests that drawPlayerCard throws an exception when the player is not found.
     */
    @Test
    void drawPlayerCard_PlayerNotFound_ThrowsException() {
        assertThrows(PlayerManagementException.class, () -> playerManagement.drawPlayerCard(game.getGameId(), user));
    }

    /**
     * Tests that drawPlayerCard draws a card for a valid player.
     *
     * @throws PlayerManagementException if an error occurs while drawing the card
     */
    @Test
    void drawPlayerCard_ValidPlayer_DrawsCard() throws PlayerManagementException {
        game.getPlayers()
            .add(player);
        when(player.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testUser");

        ICardDTO cardDTO = playerManagement.drawPlayerCard(game.getGameId(), user);

        assertNotNull(cardDTO);
    }

    /**
     * Tests that drawPlayerCard increases the infection counter when an EpidemicCard is drawn.
     *
     * @throws PlayerManagementException if an error occurs while drawing the card
     */
    @Test
    void drawPlayerCard_EpidemicCard_IncreasesInfectionCounter() throws PlayerManagementException {
        game.getPlayers()
            .add(player);
        game.getPlayerCardDrawPile()
            .removeIf(currentCard -> !(currentCard instanceof EpidemicCard));
        game.getPlayerCardDrawPile()
            .add(epidemicCard);
        game.setState(new DrawCardState());
        when(player.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testUser");
        when(gameManagement.drawInfectionCard(game)).thenReturn(new InfectionCard(1, "test", mock(ICity.class)));

        playerManagement.drawPlayerCard(game.getGameId(), user);

        assertEquals(2, game.getInfectionCounter());
    }

    /**
     * Tests that setStartingPosition throws an exception when an invalid city is provided.
     */
    @Test
    void setStartingPosition_InvalidCity_ThrowsException() {
        CityName requestedCity = CityName.ALBACETE;
        CityName heldCity = CityName.ZARAGOZA;

        ICity heldCityObject = mock(ICity.class);
        when(heldCityObject.getName()).thenReturn(heldCity);

        CityCard heldCityCard = new CityCard(1, "test", heldCityObject);
        when(player.getCards()).thenReturn(List.of(heldCityCard));

        PlayerManagementException thrown = assertThrows(
                PlayerManagementException.class,
                () -> playerManagement.setStartingPosition(game.getGameId(), requestedCity, player),
                "Expected setStartingPosition() to throw, but it did not"
        );

        assertTrue(thrown.getMessage()
                         .contains("Keine valide Stadt ausgewählt"));
    }

    /**
     * Tests that addCard adds a card to the player's hand.
     */
    @Test
    void addCard_AddsCardToPlayer() {
        ICard card = mock(ICard.class);
        List<ICard> mockCards = mock(List.class);
        when(player.getCards()).thenReturn(mockCards);

        playerManagement.addCard(player, card);

        verify(mockCards, times(1)).add(card);
    }

    /**
     * Tests that discardCard discards a single card from the player's hand.
     */
    @Test
    void discardCard_DiscardSingleCard() {
        ICard infectionCard = new InfectionCard(1, "test", mock(ICity.class));

        playerManagement.discardCard(game.getGameId(), player, infectionCard);

        assertTrue(game.getPlayerCardDiscardPile()
                       .contains(infectionCard));
    }

    /**
     * Tests that discardCards discards multiple cards from the player's hand.
     */
    @Test
    void discardCards_DiscardMultipleCards() {
        ICard card1 = new InfectionCard(1, "test", mock(ICity.class));
        ICard card2 = new CityCard(1, "test", mock(ICity.class));
        List<ICard> cards = List.of(card1, card2);

        playerManagement.discardCards(game.getGameId(), player, cards);

        assertTrue(game.getPlayerCardDiscardPile()
                       .contains(card1));
        assertTrue(game.getPlayerCardDiscardPile()
                       .contains(card2));
    }

    /**
     * Tests that setPlayerLocation sets the player's location to the specified city.
     *
     * @throws PlayerManagementException if an error occurs while setting the player's location
     */
    @Test
    void testSetPlayerLocation() throws PlayerManagementException {
        ICity city = new City(1, PlagueName.CHOLERA, CityName.ALBACETE, 1234, false);
        when(cityManagement.getCity(game.getGameId(), 1)).thenReturn(city);

        IUser testUser = new User("testUser", "testPassword");
        IPlayer testPlayer = new Player(testUser);
        game.getPlayers()
            .add(testPlayer);
        GameStore.getInstance()
                 .addGame(game.getGameId(), game);

        playerManagement.setPlayerLocation(game.getGameId(), "testUser", 1);
        assertEquals(city, testPlayer.getCurrentPosition());
    }
}
