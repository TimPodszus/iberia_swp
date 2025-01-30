package de.uol.swp.server.player.management;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.server.cards.*;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagement;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.usermanagement.IUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerManagementTest {

    private final IGame game = new Game(1, "123");

    @Mock
    private Player player;
    @Mock
    private IUser user;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private EpidemicCard epidemicCard;
    @Mock
    private GameManagement gameManagement;

    private PlayerManagement playerManagement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playerManagement = new PlayerManagement();
        GameStore.getInstance().addGame(game.getGameId(), game);
    }

    @Test
    void drawPlayerCard_PlayerNotFound_ThrowsException() {
        assertThrows(PlayerManagementException.class, () -> playerManagement.drawPlayerCard(game.getGameId(), user));
    }

    @Test
    void drawPlayerCard_ValidPlayer_DrawsCard() throws PlayerManagementException {
        game.getPlayers().add(player);
        when(player.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testUser");

        ICardDTO cardDTO = playerManagement.drawPlayerCard(game.getGameId(), user);

        assertNotNull(cardDTO);
    }


    @Test
    void drawPlayerCard_EpidemicCard_IncreasesInfectionCounter() throws PlayerManagementException {
        game.getPlayers().add(player);
        game.getPlayerCardDrawPile().removeIf(currentCard -> !(currentCard instanceof EpidemicCard));
        game.getPlayerCardDrawPile().add(epidemicCard);
        game.setState(new DrawCardState());
        when(player.getUser()).thenReturn(user);
        when(user.getUsername()).thenReturn("testUser");
        when(gameManagement.drawInfectionCard(game)).thenReturn(new InfectionCard(1, "test", mock(ICity.class)));

        playerManagement.drawPlayerCard(game.getGameId(), user);

        assertEquals(2, game.getInfectionCounter());
    }

    @Test
    void setStartingPosition_ValidCity_SetsPosition() throws PlayerManagementException {
        CityName cityName = CityName.ALBACETE;
        ICity city = mock(ICity.class);
        when(city.getName()).thenReturn(cityName);
        when(cityRepository.getCityByName(cityName)).thenReturn(city);

        CityCard cityCard = mock(CityCard.class);
        when(cityCard.getCity()).thenReturn(city);
        when(player.getCards()).thenReturn(List.of(cityCard));

        playerManagement.setStartingPosition(game.getGameId(), cityName, player);

        verify(player, times(1)).setCurrentPosition(any(ICity.class));
    }

    @Test
    void setStartingPosition_InvalidCity_ThrowsException() {
        CityName requestedCity = CityName.ALBACETE;
        CityName heldCity = CityName.ZARAGOZA;

        ICity heldCityObject = mock(ICity.class);
        when(heldCityObject.getName()).thenReturn(heldCity);

        CityCard heldCityCard = new CityCard(1, "test", heldCityObject);
        when(player.getCards()).thenReturn(List.of(heldCityCard));

        when(cityRepository.getCityByName(requestedCity)).thenReturn(null);

        PlayerManagementException thrown = assertThrows(
                PlayerManagementException.class,
                () -> playerManagement.setStartingPosition(game.getGameId(), requestedCity, player),
                "Expected setStartingPosition() to throw, but it did not"
        );

        assertTrue(thrown.getMessage().contains("Keine valide Stadt ausgewählt"));
    }

    @Test
    void addCard_AddsCardToPlayer() {
        ICard card = mock(ICard.class);
        List<ICard> mockCards = mock(List.class);
        when(player.getCards()).thenReturn(mockCards);

        playerManagement.addCard(player, card);

        verify(mockCards, times(1)).add(card);
    }

    @Test
    void discardCard_DiscardSingleCard() {
        ICard infectionCard = new InfectionCard(1, "test", mock(ICity.class));

        playerManagement.discardCard(game.getGameId(), player, infectionCard);

        assertTrue(game.getPlayerCardDiscardPile().contains(infectionCard));
    }

    @Test
    void discardCards_DiscardMultipleCards() {
        ICard card1 = new InfectionCard(1, "test", mock(ICity.class));
        ICard card2 = new CityCard(1, "test", mock(ICity.class));
        List<ICard> cards = List.of(card1, card2);

        playerManagement.discardCards(game.getGameId(), player, cards);

        assertTrue(game.getPlayerCardDiscardPile().contains(card1));
        assertTrue(game.getPlayerCardDiscardPile().contains(card2));
    }
}