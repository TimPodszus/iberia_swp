package de.uol.swp.server.connection;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.dto.DestinationInfo;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.City;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.connection.data.Connection;
import de.uol.swp.server.connection.data.IConnection;
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
    ConnectionManagement connectionManagement = new ConnectionManagement();
    IGame game;
    Player player;
    ConnectionRepository connectionRepository;
    CityRepository cityRepository;
    ICity city;

    @BeforeEach
    void setUp() {
        game = mock(Game.class);
        player = mock(Player.class);
        connectionRepository = mock(ConnectionRepository.class);
        cityRepository = mock(CityRepository.class);
        city = mock(City.class);
        GameStore.getInstance()
                 .addGame("lobbyCode", game);

        when(game.getCityRepository()).thenReturn(new CityRepository());
        when(game.getConnectionRepository()).thenReturn(new ConnectionRepository());
    }

    @Test
    void testGetConnection() {
        IConnection connection = new Connection(1, List.of(CityName.ALBACETE, CityName.ALICANTE), true, true);
        when(game.getConnectionRepository()).thenReturn(connectionRepository);
        when(connectionRepository.getConnectionByID(1)).thenReturn(connection);

        IConnection result = connectionManagement.getConnection("lobbyCode", 1);

        assertEquals(connection, result, "Expected the connection with ID 1");
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
        Map<Integer, DestinationInfo> cities = connectionManagement.getAvailableDestinations("lobbyCode", city.getId());

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

        Map<Integer, DestinationInfo> cities = connectionManagement.getAvailableDestinations(
                "lobbyCode",
                evora.getId()
        );

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

        Map<Integer, DestinationInfo> cities = connectionManagement.getAvailableDestinations("lobbyCode", city.getId());

        assertEquals(5, cities.size(), "Expected 5 available destinations for Palma de Mallorca");
        assertTrue(cities.containsKey(harbourCity.getId()), "Expected Alicante to be an available destination");
        assertFalse(
                cities.get(harbourCity.getId())
                      .getCardsUsableForMove()
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

        Map<Integer, DestinationInfo> cities = connectionManagement.getAvailableDestinations(
                "lobbyCode",
                city.getId()
        );

        assertEquals(18, cities.size(), "Expected 18 available destinations for Palma de Mallorca");
        assertTrue(cities.containsKey(harbourCity.getId()), "Expected Alicante to be an available destination");
        assertTrue(
                cities.get(harbourCity.getId())
                      .getCardsUsableForMove()
                      .isEmpty(),
                "Expected Alicante to be accessible without discarding a city card, because the player is a Sailor"
        );
    }

    /**
     * Tests the retrieval of all available destinations for all cities.
     * It verifies that the number of available destinations is as expected.
     */
    @Test
    void testGetAllDestinations() {
        when(game.getCityRepository()).thenReturn(new CityRepository());
        when(game.getConnectionRepository()).thenReturn(new ConnectionRepository());
        when(game.getCurrentPlayer()).thenReturn(player);
        when(player.getCards()).thenReturn(new ArrayList<>());

        Map<Integer, DestinationInfo> cities = connectionManagement.getAllDestinations("lobbyCode");

        assertEquals(48, cities.size(), "Expected 48 available destinations for all cities");
    }

    @Test
    void testGetBuildableTrainTracks_WhenTracksLeftZero() {
        String lobbyId = "lobbyCode";
        int cityId = 1;

        when(game.getTracksLeft()).thenReturn(-5);

        List<IConnection> result = connectionManagement.getBuildableTrainTracks(lobbyId, cityId);

        assertEquals(0, result.size(), "Expected no buildable train tracks");
    }

    @Test
    void testGetBuildableTrainTracks_WhenTracksLeftGreaterThanZero() {
        String lobbyId = "lobbyCode";
        int cityId = 1;

        when(game.getTracksLeft()).thenReturn(5);
        when(game.getCityRepository()).thenReturn(cityRepository);
        when(game.getConnectionRepository()).thenReturn(connectionRepository);

        when(cityRepository.getCity(cityId)).thenReturn(new City(1, PlagueName.MALARIA, CityName.ALICANTE, 1, false));

        List<IConnection> connections = List.of(
                new Connection(
                        1,
                        List.of(CityName.ALICANTE, CityName.BARCELONA),
                        false,
                        true
                ),
                new Connection(2, List.of(CityName.ALICANTE, CityName.ZARAGOZA), true, true)
        );

        when(connectionRepository.getConnections()).thenReturn(connections);

        List<IConnection> result = connectionManagement.getBuildableTrainTracks(lobbyId, cityId);

        assertEquals(1, result.size(), "Expected only one buildable train track");
        assertTrue(
                result.get(0)
                      .getCityNames()
                      .contains(CityName.BARCELONA), "City should be Barcelona"
        );
        assertTrue(
                result.get(0)
                      .getCityNames()
                      .contains(CityName.ALICANTE), "City should be Alicante"
        );
        assertFalse(
                result.get(0)
                      .isTrainTrack(), "The connection should not be a train track"
        );
    }
}
