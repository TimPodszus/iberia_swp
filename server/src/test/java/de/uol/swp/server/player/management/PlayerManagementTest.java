package de.uol.swp.server.player.management;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.uol.swp.common.cards.CardType;
import de.uol.swp.common.city.CityName;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.usermanagement.IUser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playerManagement = new PlayerManagement();
    }


    @Test
    void drawPlayerCard_PlayerNotFound_ThrowsException() {
        when(game.getPlayers()).thenReturn(List.of());
        assertThrows(PlayerManagementException.class, () -> playerManagement.drawPlayerCard(game, user));
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

        playerManagement.setStartingPosition(cityName, player);

        verify(player, times(1)).setCurrentPosition(any(ICity.class));
    }

    @Test
    void setStartingPosition_InvalidCity_ThrowsException() {
        CityName requestedCity = CityName.ALBACETE;
        CityName heldCity = CityName.ZARAGOZA;

        ICity heldCityObject = mock(ICity.class);
        when(heldCityObject.getName()).thenReturn(heldCity);

        CityCard heldCityCard = new CityCard(1, "test", CardType.CITY_CARD, heldCityObject);
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

}
