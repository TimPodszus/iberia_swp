package de.uol.swp.server.city.management;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.StateType;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.connection.management.ConnectionManagement;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagement;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.infection.management.InfectionManagement;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.region.management.RegionManagement;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for CityManagement.
 */
public class CityManagementTest {
    private static final String LOBBY_CODE = "lobbyCode";

    private final IGame game = new Game(1, LOBBY_CODE);

    private final ICity city = game.getCityRepository()
                                   .getCityByName(CityName.BARCELONA);

    InfectionCard infectionCard = new InfectionCard(1, "Barcelona", city);

    private ICityManagement cityManagement;

    @Mock
    private IPlayerManagement playerManagement;
    @Mock
    private RegionManagement regionManagement;
    @Mock
    private ConnectionManagement connectionManagement;
    @InjectMocks
    private final GameManagement gameManagement = new GameManagement(
            playerManagement,
            cityManagement,
            connectionManagement,
            regionManagement
    );
    private final InfectionManagement infectionManagement = new InfectionManagement();

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cityManagement = new CityManagement(regionManagement, gameManagement, infectionManagement);
        when(regionManagement.reduceWaterTreatments(any(IGame.class), any(ICity.class), anyInt())).thenReturn(1);
        GameStore.getInstance()
                 .addGame(LOBBY_CODE, game);
    }

    /**
     * Tests that infecting a city with its own plague does not escalate.
     */
    @Test
    void testInfectCityWithOwnPlagueDoesNotEscalate() {
        cityManagement.infectCityWithOwnPlague(game, infectionCard, 1);

        assertTrue(game.getCityRepository()
                       .getCityByName(CityName.BARCELONA)
                       .getInfections()
                       .stream()
                       .anyMatch(infection -> infection.getSeverity() > 0));
    }

    /**
     * Tests that infecting a city with its own plague escalates once.
     */
    @Test
    void testInfectCityWithOwnPlagueDoesEscalateOnce() {
        game.getCityRepository()
            .getCityByName(CityName.BARCELONA)
            .getInfections()
            .stream()
            .filter(i -> i.getPlagueName() == city.getPlagueName())
            .findFirst()
            .ifPresent(i -> i.setSeverity(3));

        cityManagement.infectCityWithOwnPlague(game, infectionCard, 1);

        List<CityName> cityNames = game.getConnectionRepository()
                                       .getCityNamesOfConnectedCitiesByCityName(CityName.BARCELONA);

        List<ICity> connectedCitys = game.getCityRepository()
                                         .getCitiesByNames(cityNames);

        for (ICity connectedCity : connectedCitys) {
            assertTrue(connectedCity.getInfections()
                                    .stream()
                                    .filter(infection -> infection.getPlagueName() == this.city.getPlagueName())
                                    .findFirst()
                                    .map(infection -> infection.getSeverity() == 1)
                                    .orElse(false));
        }

        assertEquals(1, game.getEscalationStage());

    }

    /**
     * Tests that infecting a city with its own plague escalates twice.
     */
    @Test
    void testInfectCityWithOwnPlagueDoesEscalateTwice() {
        game.getCityRepository()
            .getCityByName(CityName.BARCELONA)
            .getInfections()
            .stream()
            .filter(i -> i.getPlagueName() == city.getPlagueName())
            .findFirst()
            .ifPresent(i -> i.setSeverity(3));

        game.getCityRepository()
            .getCityByName(CityName.PALMA_DE_MALLORCA)
            .getInfections()
            .stream()
            .filter(i -> i.getPlagueName() == city.getPlagueName())
            .findFirst()
            .ifPresent(i -> i.setSeverity(3));

        cityManagement.infectCityWithOwnPlague(game, infectionCard, 1);

        assertEquals(2, game.getEscalationStage());
    }

    /**
     * Tests that infecting a city with its own plague with invalid parameters throws an exception.
     */
    @Test
    void testInfectCityWithOwnPlagueWithInvalidParameters() {
        assertThrows(
                CityManagementException.class,
                () -> cityManagement.infectCityWithOwnPlague(game, infectionCard, -1)
        );
    }

    /**
     * Tests that infecting a city with its own plague with enough water treatments does not increase severity.
     */
    @Test
    void testInfectCityWithOwnPlagueWithEnoughWaterTreatments() {
        game.getRegionRepository()
            .getRegionsByCityName(CityName.BARCELONA)
            .get(0)
            .increaseWaterTreatments(3);

        cityManagement.infectCityWithOwnPlague(game, infectionCard, 3);

        assertTrue(game.getCityRepository()
                       .getCityByName(CityName.BARCELONA)
                       .getInfections()
                       .stream()
                       .anyMatch(infection -> infection.getSeverity() == 0));
    }

    /**
     * Tests that infecting a city with its own plague when no cubes are left changes the game state to Game Over.
     */
    @Test
    void testInfectCityWithOwnPlagueNoCubesLeft() {
        game.getPlagueRepository()
            .getPlagues()
            .stream()
            .filter(plague -> plague.getName() == city.getPlagueName())
            .findFirst()
            .ifPresent(plague -> plague.setCubesRemaining(0));

        cityManagement.infectCityWithOwnPlague(game, infectionCard, 1);

        assertEquals(
                StateType.END_GAME_STATE,
                game.getState()
                    .getStateType()
        );
    }

    /**
     * Tests the getCity method of CityManagement.
     */
    @Test
    void testGetCity() {
        IGame mockGame = mock(IGame.class);
        when(mockGame.getGameId()).thenReturn("lobbyCode");
        GameStore.getInstance()
                 .addGame(mockGame.getGameId(), mockGame);

        when(mockGame.getCityRepository()).thenReturn(new CityRepository());


        ICity cityToFind = cityManagement.getCity("lobbyCode", 1);

        assertEquals(1, cityToFind.getId());
    }

    @Test
    void testBuildHospitalSucceeds() throws PlayerManagementException {
        IUser user = new User("username", "password");
        IPlayer player = new Player(user);
        player.setCurrentPosition(game.getCityRepository()
                                      .getCityByName(CityName.PALMA_DE_MALLORCA));
        game.getPlayers()
            .add(player);
        game.setCurrentPlayerIndex(0);

        CityCard cityCard = new CityCard(
                1,
                CityName.PALMA_DE_MALLORCA.getDisplayName(),
                game.getCityRepository()
                    .getCityByName(CityName.PALMA_DE_MALLORCA)
        );
        player.getCards()
              .add(cityCard);

        cityManagement.buildHospital(
                LOBBY_CODE,
                "username",
                game.getCityRepository()
                    .getCityByName(CityName.PALMA_DE_MALLORCA)
                    .getId()
        );
        assertTrue(game.getCityRepository()
                       .getCityByName(CityName.PALMA_DE_MALLORCA)
                       .isHospitalBuilt());
    }

    @Test
    void testBuildHospitalWithEventCardWithWrongColor() {
        IUser user = new User("username", "password");
        IPlayer player = new Player(user);
        player.setCurrentPosition(game.getCityRepository()
                                      .getCityByName(CityName.PALMA_DE_MALLORCA));
        game.getPlayers()
            .add(player);
        game.setCurrentPlayerIndex(0);

        int cityId = game.getCityRepository()
                         .getCityByName(CityName.MADRID)
                         .getId();

        assertThrows(
                CityManagementException.class,
                () -> cityManagement.buildHospitalWithEventCard(LOBBY_CODE, cityId)
        );
    }
}