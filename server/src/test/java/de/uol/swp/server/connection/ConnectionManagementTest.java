package de.uol.swp.server.connection;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.City;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.connection.management.ConnectionManagement;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.role.Nurse;
import de.uol.swp.server.role.Sailor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConnectionManagementTest {
    final ConnectionManagement connectionManagement = new ConnectionManagement();
    IGame game;
    Player player;

    @BeforeEach
    void setUp() {
        game = mock(Game.class);
        player = mock(Player.class);
        GameStore.getInstance()
                 .addGame("lobbyCode", game);

        when(game.getCityRepository()).thenReturn(new CityRepository());
        when(game.getConnectionRepository()).thenReturn(new ConnectionRepository());
    }

    /**
     * Tests the available destinations for the city Palma de Mallorca.
     * It verifies that the number of available destinations is as expected.
     */
    @Test
    void testAvailableDestinations() {
        when(game.getCityRepository()).thenReturn(new CityRepository());
        when(game.getConnectionRepository()).thenReturn(new ConnectionRepository());
        when(game.getCurrentPlayer()).thenReturn(player);
        when(player.getCards()).thenReturn(new ArrayList<>());

        ICity city = new City(29, PlagueName.YELLOW_FEVER, CityName.PALMA_DE_MALLORCA, -123, true);
        Map<ICity, List<Card>> cities = connectionManagement.getAvailableDestinations("lobbyCode", city.getId());

        assertEquals(2, cities.size(), "Expected 2 available destinations for Palma de Mallorca");
    }

    @Test
    void testAvailableDestinationsWithTrainTracks() {
        ICity evora = game.getCityRepository()
                          .getCityByName(CityName.EVORA);
        game.getConnectionRepository()
            .getConnectionByID(19)
            .buildTrainTracks(true);
        game.getConnectionRepository()
            .getConnectionByID(81)
            .buildTrainTracks(true);
        game.getConnectionRepository()
            .getConnectionByID(75)
            .buildTrainTracks(true);
        game.getConnectionRepository()
            .getConnectionByID(72)
            .buildTrainTracks(true);

        when(game.getCurrentPlayer()).thenReturn(player);
        when(player.getCards()).thenReturn(new ArrayList<>());

        Map<ICity, List<Card>> cities = connectionManagement.getAvailableDestinations("lobbyCode", evora.getId());

        assertEquals(6, cities.size(), "Expected 5 available destinations for Evora");
    }

    @Test
    void testAvailableDestinationsWithHarbourConnections() {
        ICity city = game.getCityRepository()
                         .getCityByName(CityName.PALMA_DE_MALLORCA);
        ICity harbourCity = game.getCityRepository()
                                .getCityByName(CityName.ALICANTE);

        when(game.getCurrentPlayer()).thenReturn(player);
        when(player.getCards()).thenReturn(List.of(new CityCard(
                harbourCity.getId(),
                harbourCity.getName()
                           .toString(),
                harbourCity
        )));
        when(player.getRole()).thenReturn(new Nurse());

        Map<ICity, List<Card>> cities = connectionManagement.getAvailableDestinations("lobbyCode", city.getId());

        assertEquals(6, cities.size(), "Expected 2 available destinations for Palma de Mallorca");
        assertTrue(cities.containsKey(harbourCity), "Expected Alicante to be an available destination");
        assertFalse(
                cities.get(harbourCity)
                      .isEmpty(),
                "Expected Alicante to be a harbour connection and accessible only by discarding a city card"
        );
    }

    @Test
    void testAvailableDestinationsWithHarbourConnectionsAndPlayerRoleSailor() {
        ICity city = game.getCityRepository()
                         .getCityByName(CityName.PALMA_DE_MALLORCA);
        ICity harbourCity = game.getCityRepository()
                                .getCityByName(CityName.ALICANTE);

        when(game.getCurrentPlayer()).thenReturn(player);
        when(player.getCards()).thenReturn(List.of(new CityCard(
                harbourCity.getId(),
                harbourCity.getName()
                           .toString(),
                harbourCity
        )));
        when(player.getRole()).thenReturn(new Sailor());

        Map<ICity, List<Card>> cities = connectionManagement.getAvailableDestinations("lobbyCode", city.getId());

        assertEquals(19, cities.size(), "Expected 2 available destinations for Palma de Mallorca");
        assertTrue(cities.containsKey(harbourCity), "Expected Alicante to be an available destination");
        assertTrue(
                cities.get(harbourCity)
                      .isEmpty(),
                "Expected Alicante to be accessible without discarding a city card, because the player is a Sailor"
        );
    }
}
