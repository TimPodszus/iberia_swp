package de.uol.swp.server.game.management;

import de.uol.swp.common.city.CityName;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.common.game.GameActions;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.role.IRole;
import de.uol.swp.server.role.Nurse;
import de.uol.swp.server.role.Sailor;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.WaitForPositioning;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.management.PlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static de.uol.swp.common.city.CityName.ALBACETE;
import static org.mockito.Mockito.*;

/**
 * Test class for the {@link GameManagement} class.
 * This class ensures the correct functionality of game management methods, such as creating players,
 * setting the starting player, assigning roles, and initiating infections.
 */
class GameManagementTest {

    @Mock
    private GameStore gameStore;

    @Mock
    private CreateGameRequest createGameRequest;

    @Mock
    private PositioningRequest positioningRequest;

    @Mock
    private PlayerManagement playerManagement;

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
                 .addGame("lobbyCode", game);
        cityRepository = new CityRepository();

        UserDTO userDTO1 = new UserDTO("test", "test");
        UserDTO userDTO2 = new UserDTO("test2", "test2");

        when(createGameRequest.getDifficulty()).thenReturn(1);
        when(createGameRequest.getLobbyId()).thenReturn("lobby123");
        when(createGameRequest.getUsers()).thenReturn(List.of(userDTO1, userDTO2));
    }

    @Test
    void testCreateAndInitializeGame() {
        ArgumentCaptor<IGame> gameCaptor = ArgumentCaptor.forClass(IGame.class);
        gameManagement.createAndInitializeGame(createGameRequest);
        verify(gameStore).addGame(eq("lobby123"), gameCaptor.capture());
        IGame capturedGame = gameCaptor.getValue();
        assertNotNull(capturedGame);
        assertEquals(1, capturedGame.getDifficulty());
        assertEquals("lobby123", capturedGame.getGameId());
    }

    @Test
    void testSetPositioning_InvalidLobbyCode() {
        when(gameStore.getGame("invalidLobby")).thenReturn(null);
        when(positioningRequest.getLobbyId()).thenReturn("invalidLobby");

        assertThrows(NullPointerException.class, () -> gameManagement.setPositioning(positioningRequest));
    }

    @Test
    void testSetPositioning_InvalidGameState() throws PlayerManagementException {
        IGame mockGame = mock(IGame.class);
        when(gameStore.getGame("lobby123")).thenReturn(mockGame);
        when(mockGame.getState()).thenReturn(mock(PlayerTurnState.class));
        when(positioningRequest.getLobbyId()).thenReturn("lobby123");

        gameManagement.setPositioning(positioningRequest);
        verify(mockGame, never()).getPlayers();
    }

    @Test
    void testSetPositioning_PlayerNotFound() {
        IGame mockGame = mock(IGame.class);
        WaitForPositioning mockState = mock(WaitForPositioning.class);
        Session mockSession = mock(Session.class);
        IUserDTO mockUser = mock(IUserDTO.class);

        when(gameStore.getGame("lobby123")).thenReturn(mockGame);
        when(mockGame.getState()).thenReturn(mockState);
        when(mockGame.getPlayers()).thenReturn(List.of());
        when(positioningRequest.getLobbyId()).thenReturn("lobby123");
        when(mockSession.getUser()).thenReturn(mockUser);
        when(mockUser.getUsername()).thenReturn("testUsername");
        when(positioningRequest.getSession()).thenReturn(Optional.of(mockSession));

        assertThrows(AssertionError.class, () -> gameManagement.setPositioning(positioningRequest));
    }

    @Test
    void testSetPositioning_AllPlayersPositioned() throws PlayerManagementException {
        IGame mockGame = mock(IGame.class);
        WaitForPositioning mockState = mock(WaitForPositioning.class);
        Player mockPlayer = spy(new Player(mock(IUser.class)));
        IUser mockUser = mock(IUser.class);
        IUserDTO mockUserDTO = mock(IUserDTO.class);
        Session mockSession = mock(Session.class);
        CityRepository mockCityRepository = mock(CityRepository.class);

        when(mockUser.getUsername()).thenReturn("testUsername");
        when(mockUserDTO.getUsername()).thenReturn("testUsername");

        CityCard mockCityCard = mock(CityCard.class);
        ICity mockCity = mock(ICity.class);
        when(mockCity.getName()).thenReturn(ALBACETE);
        when(mockCityCard.getCity()).thenReturn(mockCity);

        List<Card> cards = new ArrayList<>();
        cards.add(mockCityCard);
        when(mockPlayer.getCards()).thenReturn(cards);

        List<IPlayer> playerList = new ArrayList<>();
        playerList.add(mockPlayer);

        when(gameStore.getGame("lobby123")).thenReturn(mockGame);
        when(mockGame.getState()).thenReturn(mockState);
        when(mockGame.getPlayers()).thenReturn(playerList);
        when(mockGame.getCityRepository()).thenReturn(mockCityRepository);
        when(mockCityRepository.getCitiesByNames(any(CityName.class))).thenReturn(List.of(mockCity));

        when(mockPlayer.getUser()).thenReturn(mockUser);
        when(mockUser.getUsername()).thenReturn("testUsername");

        when(positioningRequest.getLobbyId()).thenReturn("lobby123");
        when(positioningRequest.getCityId()).thenReturn(34);
        when(mockSession.getUser()).thenReturn(mockUserDTO);
        when(positioningRequest.getSession()).thenReturn(Optional.of(mockSession));

        when(mockCityRepository.getCityNameById(34)).thenReturn(ALBACETE);

        when(mockState.getPositionedPlayersCount()).thenReturn(1);

        gameManagement.setPositioning(positioningRequest);

        verify(mockGame).setState(any(PlayerTurnState.class));
        verify(mockGame).setCurrentPlayerIndex(0);
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
