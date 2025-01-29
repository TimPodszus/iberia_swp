package de.uol.swp.server.player.management;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.uol.swp.common.city.CityName;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.usermanagement.IUser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

/**
 * Test class for PlayerManagement.
 */
class PlayerManagementTest {

    @Mock
    private IGame game;
    @Mock
    private Player player;
    @Mock
    private IUser user;
    @Mock
    private CityRepository cityRepository;

    private PlayerManagement playerManagement;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playerManagement = new PlayerManagement();
    }

    /**
     * Tests that drawPlayerCard throws an exception when the player is not found.
     */
    @Test
    void drawPlayerCard_PlayerNotFound_ThrowsException() {
        when(game.getPlayers()).thenReturn(List.of());
        assertThrows(PlayerManagementException.class, () -> playerManagement.drawPlayerCard(game, user));
    }

    /**
     * Tests that setStartingPosition sets the position when a valid city is provided.
     *
     * @throws PlayerManagementException if an error occurs while setting the starting position
     */
    @Test
    void setStartingPosition_ValidCity_SetsPosition() throws PlayerManagementException {
        CityName cityName = CityName.ALBACETE;
        ICity city = mock(ICity.class);
        when(city.getName()).thenReturn(cityName);
        when(cityRepository.getCityByName(cityName)).thenReturn(city);

        CityCard cityCard = mock(CityCard.class);
        when(cityCard.getCity()).thenReturn(city);
        when(player.getCards()).thenReturn(List.of(cityCard));

        playerManagement.setStartingPosition(cityName, player);

        verify(player, times(1)).setCurrentPosition(any(ICity.class));
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

        when(cityRepository.getCityByName(requestedCity)).thenReturn(null);

        PlayerManagementException thrown = assertThrows(
                PlayerManagementException.class,
                () -> playerManagement.setStartingPosition(requestedCity, player),
                "Expected setStartingPosition() to throw, but it did not"
        );

        assertTrue(thrown.getMessage()
                         .contains("Keine valide Stadt ausgewählt"));
    }

    /**
     * Tests that getCard returns the correct card when it is found.
     *
     * @throws PlayerManagementException if an error occurs while getting the card
     */
    @Test
    void testGetCard() throws PlayerManagementException {
        String lobbyId = "testLobby";
        String playerName = "testPlayer";
        int cardId = 1;

        Card card = mock(Card.class);
        when(card.getId()).thenReturn(cardId);

        when(game.getPlayers()).thenReturn(List.of(player));
        GameStore.getInstance()
                 .addGame(lobbyId, game);

        when(player.getUser()).thenReturn(user);
        when(player.getCards()).thenReturn(List.of(card));

        when(user.getUsername()).thenReturn(playerName);

        Card result = playerManagement.getCard(lobbyId, playerName, cardId);

        assertEquals(card, result);
    }

    /**
     * Tests that getCard returns null when the card is not found.
     *
     * @throws PlayerManagementException if an error occurs while getting the card
     */
    @Test
    void testGetCardNotFound() throws PlayerManagementException {
        String lobbyId = "testLobby";
        String playerName = "testPlayer";
        int cardId = 1;

        when(game.getPlayers()).thenReturn(List.of(player));
        GameStore.getInstance()
                 .addGame(lobbyId, game);

        when(player.getUser()).thenReturn(user);
        when(player.getCards()).thenReturn(List.of());

        when(user.getUsername()).thenReturn(playerName);

        assertNull(playerManagement.getCard(lobbyId, playerName, cardId));
    }
}
