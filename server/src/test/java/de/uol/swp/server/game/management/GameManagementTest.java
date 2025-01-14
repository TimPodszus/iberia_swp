package de.uol.swp.server.game.management;

import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityName;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.role.IRole;
import de.uol.swp.server.role.Nurse;
import de.uol.swp.server.role.Sailor;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Test class for the {@link GameManagement} class.
 * This class ensures the correct functionality of game management methods, such as creating players,
 * setting the starting player, assigning roles, and initiating infections.
 */
class GameManagementTest {
    @Mock
    private IGame game;

    @InjectMocks
    private GameManagement gameManagement;

    private CityRepository cityRepository;

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        GameStore.getInstance()
                 .addGame("lobbyCode", game);
        cityRepository = new CityRepository();
    }

    @Test
    void testGameManagementException() {
        GameManagementException exception = assertThrows(GameManagementException.class, () -> {
            throw new GameManagementException("Test Exception");
        });

        assertEquals("Test Exception", exception.getMessage());
    }

    @Test
    void testMovePlayerWithWrongUser() {
        City destinationCity = cityRepository.getCityByName(CityName.BARCELONA);
        IUser user1 = new User("user1", "user1");
        IUser user2 = new User("user2", "user2");
        createTestPlayers(user1, user2);
        Player player1 = game.getPlayers()
                             .get(0);
        when(game.getCurrentPlayer()).thenReturn(player1);

        assertThrows(GameManagementException.class,
                () -> gameManagement.movePlayer(user2, "lobbyCode", destinationCity),
                "Expected GameManagementException"
        );
    }

    @Test
    void testMoveByLand() throws GameManagementException {
        City startCity = cityRepository.getCityByName(CityName.BARCELONA);
        City destinationCity = cityRepository.getCityByName(CityName.PALMA_DE_MALLORCA);
        IUser user = new User("user1");
        createTestPlayers(user);
        Player player = game.getPlayers()
                            .get(0);
        setupPlayerForMove(startCity, player, new Sailor(), new ArrayList<>());

        gameManagement.movePlayer(user, "lobbyCode", destinationCity);

        assertEquals(destinationCity,
                player.getCurrentPosition(),
                "Expected player to have moved to Palma de Mallorca"
        );
    }

    @Test
    void testMoveWithUnconnectedCities() {
        City startCity = cityRepository.getCityByName(CityName.BARCELONA);
        City destinationCity = cityRepository.getCityByName(CityName.MADRID);
        IUser user = new User("user1");
        createTestPlayers(user);
        Player player = game.getPlayers()
                            .get(0);
        setupPlayerForMove(startCity, player, new Sailor(), new ArrayList<>());

        assertThrows(GameManagementException.class,
                () -> gameManagement.movePlayer(user, "lobbyCode", destinationCity),
                "Expected GameManagementException"
        );
    }

    @Test
    void testMoveBySea() throws GameManagementException {
        City startCity = cityRepository.getCityByName(CityName.BARCELONA);
        City destinationCity = cityRepository.getCityByName(CityName.ALICANTE);
        Card destinationCityCard = new CityCard(destinationCity.getId(),
                destinationCity.getName()
                               .toString(),
                destinationCity
        );
        IUser user = new User("user1", "");
        createTestPlayers(user);
        Player player = game.getPlayers()
                            .get(0);
        setupPlayerForMove(startCity, player, new Nurse(), new ArrayList<>(List.of(destinationCityCard)));

        assertTrue(player.getCards()
                         .contains(destinationCityCard), "Expected player to have the destination city card");

        gameManagement.movePlayer(user, "lobbyCode", destinationCity);

        assertEquals(destinationCity, player.getCurrentPosition(), "Expected player to have moved to Alicante");
        assertFalse(player.getCards()
                          .contains(destinationCityCard),
                "Expected player to have discarded the destination city card"
        );
    }

    @Test
    void testMoveBySeaWithoutCityCard() {
        City startCity = cityRepository.getCityByName(CityName.BARCELONA);
        City destinationCity = cityRepository.getCityByName(CityName.ALICANTE);
        IUser user = new User("user1");
        createTestPlayers(user);
        Player player = game.getPlayers()
                            .get(0);
        setupPlayerForMove(startCity, player, new Nurse(), new ArrayList<>());

        assertThrows(GameManagementException.class,
                () -> gameManagement.movePlayer(user, "lobbyCode", destinationCity),
                "Expected GameManagementException"
        );
    }

    @Test
    void testMoveSailorBySea() throws GameManagementException {
        City startCity = cityRepository.getCityByName(CityName.BARCELONA);
        City destinationCity = cityRepository.getCityByName(CityName.ALICANTE);
        IUser user = new User("user1");
        createTestPlayers(user);
        Player player = game.getPlayers()
                            .get(0);
        setupPlayerForMove(startCity, player, new Sailor(), new ArrayList<>());

        gameManagement.movePlayer(user, "lobbyCode", destinationCity);

        assertEquals(destinationCity, player.getCurrentPosition(), "Expected player to have moved to Alicante");
    }

    @Test
    void testMoveSailorBySeaWithCityCard() throws GameManagementException {
        City startCity = cityRepository.getCityByName(CityName.BARCELONA);
        City destinationCity = cityRepository.getCityByName(CityName.ALICANTE);
        Card destinationCityCard = new CityCard(destinationCity.getId(),
                destinationCity.getName()
                               .toString(),
                destinationCity
        );
        IUser user = new User("user1");
        createTestPlayers(user);
        Player player = game.getPlayers()
                            .get(0);
        setupPlayerForMove(startCity, player, new Sailor(), new ArrayList<>(List.of(destinationCityCard)));

        assertTrue(player.getCards()
                         .contains(destinationCityCard), "Expected player to have the destination city card");

        gameManagement.movePlayer(user, "lobbyCode", destinationCity);

        assertEquals(destinationCity, player.getCurrentPosition(), "Expected player to have moved to Alicante");
        assertTrue(
                player.getCards()
                      .contains(destinationCityCard),
                "Expected player to have not discarded the destination city card, because he is the sailor"
        );
    }

    @Test
    void testTrainRide() throws GameManagementException {
        City startCity = cityRepository.getCityByName(CityName.EVORA);
        City destinationCity = cityRepository.getCityByName(CityName.VALLADOLID);
        IUser user = new User("user1");
        createTestPlayers(user);
        Player player = game.getPlayers()
                            .get(0);
        setupPlayerForMove(startCity, player, new Nurse(), new ArrayList<>());
        buildTrainTracks();

        gameManagement.movePlayer(user, "lobbyCode", destinationCity);

        assertEquals(destinationCity, player.getCurrentPosition(), "Expected player to have moved to Valladolid");
    }

    private void createTestPlayers(IUser... users) {
        List<Player> players = new ArrayList<>();
        for (IUser user : users) {
            Player player = new Player(user);
            players.add(player);
        }
        when(game.getPlayers()).thenReturn(players);
    }

    private void setupPlayerForMove(
            City startCity, Player player, IRole role, List<Card> cards
    ) {
        player.setCurrentPosition(startCity);
        player.setRole(role);
        player.setCards(cards);
        when(game.getCurrentPlayer()).thenReturn(player);
        when(game.getConnectionRepository()).thenReturn(new ConnectionRepository());
        when(game.getCityRepository()).thenReturn(cityRepository);
    }

    /**
     * Builds train tracks for specific connections.
     * This method sets the train tracks as built between evora, badajoz, cuidad real, madrid and valladolid.
     */
    private void buildTrainTracks() {
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
    }
}
