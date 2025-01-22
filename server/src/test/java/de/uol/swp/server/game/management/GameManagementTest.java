package de.uol.swp.server.game.management;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.GameActions;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.city.management.CityManagement;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.WaitForPositioning;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.player.management.PlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
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

import static de.uol.swp.common.city.CityName.ALBACETE;
import static de.uol.swp.common.city.CityName.ALICANTE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for the {@link GameManagement} class.
 * This class ensures the correct functionality of game management methods, such as creating players,
 * setting the starting player, assigning roles, and initiating infections.
 */
class GameManagementTest {

    private static final String LOBBY_CODE = "lobbyCode";

    @Mock
    private PlayerManagement playerManagement;

    @Mock
    private CityManagement cityManagement;

    @InjectMocks
    private GameManagement gameManagement;

    private CityRepository cityRepository;

    @Mock
    private IGame game;

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        GameStore.getInstance()
                 .addGame(LOBBY_CODE, game);
        cityRepository = new CityRepository();
    }

    @Test
    void testCreateAndInitializeGame() {
        List<IUserDTO> testUsers = List.of(new UserDTO("test", "test"), new UserDTO("test2", "test2"));
        CreateGameRequest request = new CreateGameRequest("lobby123", 1, testUsers);

        IGame createdGame = gameManagement.createAndInitializeGame(request);

        assertEquals(createdGame,
                GameStore.getInstance()
                         .getGame("lobby123")
        );
    }

    @Test
    void testSetPositioning_InvalidLobbyCode() {
        PositioningRequest request = new PositioningRequest("lobby123", 12);
        when(game.getState()).thenReturn(mock(WaitForPositioning.class));

        assertThrows(GameManagementException.class, () -> gameManagement.setPositioning(request));
    }

    @Test
    void testSetPositioning_InvalidGameState() {
        when(game.getState()).thenReturn(mock(PlayerTurnState.class));
        PositioningRequest request = new PositioningRequest(LOBBY_CODE, 12);

        assertThrows(GameManagementException.class,
                () -> gameManagement.setPositioning(request),
                "Expected GameManagementException"
        );
    }

    @Test
    void testSetPositioning_PlayerNotFound() {
        IUser testUser = new User("test", "test");
        Session session = UUIDSession.create(testUser);
        PositioningRequest request = new PositioningRequest(LOBBY_CODE, 12);
        request.setSession(session);
        WaitForPositioning mockState = mock(WaitForPositioning.class);
        when(game.getState()).thenReturn(mockState);
        when(game.getPlayers()).thenReturn(List.of());

        assertThrows(AssertionError.class, () -> gameManagement.setPositioning(request));
    }

    @Test
    void testSetPositioning_AllPlayersPositioned() throws GameManagementException {
        WaitForPositioning mockState = mock(WaitForPositioning.class);
        when(mockState.getPositionedPlayersCount()).thenReturn(1);
        when(game.getState()).thenReturn(mockState);

        IUser testUser = new User("test", "test");
        Session session = UUIDSession.create(testUser);
        IPlayer player = new Player(testUser);
        when(game.getPlayers()).thenReturn(List.of(player));

        ICity albacete = cityRepository.getCityByName(ALBACETE);
        CityCard cityCard = new CityCard(albacete.getId(),
                albacete.getName()
                        .toString(),
                albacete
        );
        player.addCard(cityCard);

        PositioningRequest request = new PositioningRequest(LOBBY_CODE, 34);
        request.setSession(session);

        when(game.getCityRepository()).thenReturn(cityRepository);

        gameManagement.setPositioning(request);

        verify(game).setState(any(PlayerTurnState.class));
        verify(game).setCurrentPlayerIndex(0);
    }

    @Test
    void testSetPositioningWithPlayerManagementException() throws PlayerManagementException {
        WaitForPositioning mockState = mock(WaitForPositioning.class);
        when(mockState.getPositionedPlayersCount()).thenReturn(1);
        when(game.getState()).thenReturn(mockState);

        IUser testUser = new User("test", "test");
        Session session = UUIDSession.create(testUser);
        IPlayer player = new Player(testUser);
        when(game.getPlayers()).thenReturn(List.of(player));
        doThrow(PlayerManagementException.class).when(playerManagement)
                                                .setStartingPosition(ALBACETE, player);

        PositioningRequest request = new PositioningRequest(LOBBY_CODE, 34);
        request.setSession(session);

        when(game.getCityRepository()).thenReturn(cityRepository);

        assertThrows(GameManagementException.class, () -> gameManagement.setPositioning(request));
    }

    @Test
    void testDrawInfectionCard() {
        InfectionCard infectionCard1 = new InfectionCard(1,
                "InfectionCard",
                cityRepository.getCityByName(CityName.BARCELONA)
        );
        InfectionCard infectionCard2 = new InfectionCard(2, "InfectionCard", cityRepository.getCityByName(ALICANTE));
        when(game.getInfectionCardDrawPile()).thenReturn(new ArrayList<>(List.of(infectionCard1, infectionCard2)));

        InfectionCard drawnCard = gameManagement.drawInfectionCard(game);

        assertEquals(infectionCard1, drawnCard, "Expected Barcelona infection card to be drawn");
    }

    @Test
    void testDrawInfectionCardWithEmptyInfectionDeck() {
        when(game.getInfectionCardDrawPile()).thenReturn(new ArrayList<>());

        assertThrows(IllegalStateException.class, () -> gameManagement.drawInfectionCard(game));
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
        ICity destinationCity = cityRepository.getCityByName(CityName.BARCELONA);
        IUser user1 = new User("user1", "user1");
        IUser user2 = new User("user2", "user2");
        createTestPlayers(user1, user2);
        IPlayer player1 = game.getPlayers()
                              .get(0);
        when(game.getCurrentPlayer()).thenReturn(player1);

        assertThrows(GameManagementException.class,
                () -> gameManagement.movePlayer(user2, "lobbyCode", destinationCity),
                "Expected GameManagementException"
        );
    }

    @Test
    void testMoveByLand() throws GameManagementException {
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.PALMA_DE_MALLORCA);
        IUser user = new User("user1");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
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
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.MADRID);
        IUser user = new User("user1");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
                             .get(0);
        setupPlayerForMove(startCity, player, new Sailor(), new ArrayList<>());

        assertThrows(GameManagementException.class,
                () -> gameManagement.movePlayer(user, "lobbyCode", destinationCity),
                "Expected GameManagementException"
        );
    }

    @Test
    void testMoveBySea() throws GameManagementException {
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.ALICANTE);
        Card destinationCityCard = new CityCard(destinationCity.getId(),
                destinationCity.getName()
                               .toString(),
                destinationCity
        );
        IUser user = new User("user1", "");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
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
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.ALICANTE);
        IUser user = new User("user1");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
                             .get(0);
        setupPlayerForMove(startCity, player, new Nurse(), new ArrayList<>());

        assertThrows(GameManagementException.class,
                () -> gameManagement.movePlayer(user, "lobbyCode", destinationCity),
                "Expected GameManagementException"
        );
    }

    @Test
    void testMoveSailorBySea() throws GameManagementException {
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.ALICANTE);
        IUser user = new User("user1");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
                             .get(0);
        setupPlayerForMove(startCity, player, new Sailor(), new ArrayList<>());

        gameManagement.movePlayer(user, "lobbyCode", destinationCity);

        assertEquals(destinationCity, player.getCurrentPosition(), "Expected player to have moved to Alicante");
    }

    @Test
    void testMoveSailorBySeaWithCityCard() throws GameManagementException {
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.ALICANTE);
        Card destinationCityCard = new CityCard(destinationCity.getId(),
                destinationCity.getName()
                               .toString(),
                destinationCity
        );
        IUser user = new User("user1");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
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
        ICity startCity = cityRepository.getCityByName(CityName.EVORA);
        ICity destinationCity = cityRepository.getCityByName(CityName.VALLADOLID);
        IUser user = new User("user1");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
                             .get(0);
        setupPlayerForMove(startCity, player, new Nurse(), new ArrayList<>());
        buildTrainTracks();

        gameManagement.movePlayer(user, "lobbyCode", destinationCity);

        assertEquals(destinationCity, player.getCurrentPosition(), "Expected player to have moved to Valladolid");
    }

    private void createTestPlayers(IUser... users) {
        List<IPlayer> players = new ArrayList<>();
        for (IUser user : users) {
            IPlayer player = new Player(user);
            players.add(player);
        }
        when(game.getPlayers()).thenReturn(players);
    }

    private void setupPlayerForMove(
            ICity startCity, IPlayer player, IRole role, List<Card> cards
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

    @Test
    void testGetAvailableActions() {
        List<GameActions> actions = gameManagement.getAvailableActions("LobbyId", null);
        assertEquals(6, actions.size());
    }
}
