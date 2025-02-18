package de.uol.swp.server.game.management;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.GameActions;
import de.uol.swp.common.game.StateType;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.cards.data.eventcards.OnTheMoveDayAndNightEventCard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.city.management.CityManagement;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.connection.data.Connection;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.connection.management.IConnectionManagement;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.states.*;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.player.management.PlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.role.IRole;
import de.uol.swp.server.role.Nurse;
import de.uol.swp.server.role.RailwayWorker;
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
import java.util.Map;

import static de.uol.swp.common.city.CityName.*;
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

    @Mock
    private IConnectionManagement connectionManagement;

    @InjectMocks
    private GameManagement gameManagement;

    @Mock
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
        when(game.getGameId()).thenReturn(LOBBY_CODE);
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
        playerManagement.addCard(player, cityCard);

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
        IPlayer player = new Player(testUser);
        when(game.getPlayers()).thenReturn(List.of(player));
        when(game.getGameId()).thenReturn("gameId");
        doThrow(PlayerManagementException.class).when(playerManagement)
                                                .setStartingPosition("gameId", ALBACETE, player);

        PositioningRequest request = new PositioningRequest(LOBBY_CODE, 34);

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
                () -> gameManagement.movePlayer(user2, "lobbyCode", destinationCity, null),
                "Expected GameManagementException"
        );
    }

    @Test
    void testMovePlayerWithWrongGameState() {
        ICity destinationCity = cityRepository.getCityByName(CityName.BARCELONA);
        IUser user = new User("user1");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
                             .get(0);
        when(game.getCurrentPlayer()).thenReturn(player);
        when(game.getState()).thenReturn(mock(WaitForPositioning.class));

        assertThrows(GameManagementException.class,
                () -> gameManagement.movePlayer(user, "lobbyCode", destinationCity, null),
                "Expected GameManagementException"
        );
    }

    @Test
    void testMoveByLand() throws GameManagementException {
        Map<ICity, List<ICard>> availableDestinations = Map.of(
                cityRepository.getCityByName(CityName.PALMA_DE_MALLORCA),
                List.of()
        );
        when(connectionManagement.getAvailableDestinations(LOBBY_CODE, 27)).thenReturn(availableDestinations);
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.PALMA_DE_MALLORCA);
        IUser user = new User("user1");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
                             .get(0);
        setupPlayerForMove(startCity, player, new Sailor(), new ArrayList<>());

        gameManagement.movePlayer(user, "lobbyCode", destinationCity, null);

        assertEquals(destinationCity,
                player.getCurrentPosition(),
                "Expected player to have moved to Palma de Mallorca"
        );
        assertEquals(3,
                ((PlayerTurnState) game.getState()).getActionsRemaining(),
                "Expected player to have 3 actions left"
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
                () -> gameManagement.movePlayer(user, "lobbyCode", destinationCity, null),
                "Expected GameManagementException"
        );
    }

    @Test
    void testMoveBySea() throws GameManagementException {
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.ALICANTE);
        ICard destinationCityCard = new CityCard(destinationCity.getId(),
                destinationCity.getName()
                               .toString(),
                destinationCity
        );

        Map<ICity, List<ICard>> availableDestinations = Map.of(
                cityRepository.getCityByName(CityName.ALICANTE),
                List.of(destinationCityCard)
        );
        when(connectionManagement.getAvailableDestinations(LOBBY_CODE, 27)).thenReturn(availableDestinations);

        IUser user = new User("user1", "");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
                             .get(0);
        setupPlayerForMove(startCity, player, new Nurse(), new ArrayList<>(List.of(destinationCityCard)));

        assertTrue(player.getCards()
                         .contains(destinationCityCard), "Expected player to have the destination city card");

        gameManagement.movePlayer(user, "lobbyCode", destinationCity, destinationCityCard);

        assertEquals(destinationCity, player.getCurrentPosition(), "Expected player to have moved to Alicante");
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
                () -> gameManagement.movePlayer(user, "lobbyCode", destinationCity, null),
                "Expected GameManagementException"
        );
    }

    @Test
    void testMoveSailorBySea() throws GameManagementException {
        Map<ICity, List<ICard>> availableDestinations = Map.of(
                cityRepository.getCityByName(CityName.ALICANTE),
                List.of()
        );
        when(connectionManagement.getAvailableDestinations(LOBBY_CODE, 27)).thenReturn(availableDestinations);
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.ALICANTE);
        IUser user = new User("user1");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
                             .get(0);
        setupPlayerForMove(startCity, player, new Sailor(), new ArrayList<>());

        gameManagement.movePlayer(user, "lobbyCode", destinationCity, null);

        assertEquals(destinationCity, player.getCurrentPosition(), "Expected player to have moved to Alicante");
    }

    @Test
    void testMoveSailorBySeaWithCityCard() throws GameManagementException {
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.ALICANTE);
        ICard destinationCityCard = new CityCard(destinationCity.getId(),
                destinationCity.getName()
                               .toString(),
                destinationCity
        );

        Map<ICity, List<ICard>> availableDestinations = Map.of(
                cityRepository.getCityByName(CityName.ALICANTE),
                List.of(destinationCityCard)
        );
        when(connectionManagement.getAvailableDestinations(LOBBY_CODE, 27)).thenReturn(availableDestinations);

        IUser user = new User("user1");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
                             .get(0);
        setupPlayerForMove(startCity, player, new Sailor(), new ArrayList<>(List.of(destinationCityCard)));

        assertTrue(player.getCards()
                         .contains(destinationCityCard), "Expected player to have the destination city card");

        gameManagement.movePlayer(user, "lobbyCode", destinationCity, destinationCityCard);

        assertEquals(destinationCity, player.getCurrentPosition(), "Expected player to have moved to Alicante");
        assertTrue(
                player.getCards()
                      .contains(destinationCityCard),
                "Expected player to have not discarded the destination city card, because he is the sailor"
        );
    }

    @Test
    void testTrainRide() throws GameManagementException {
        Map<ICity, List<ICard>> availableDestinations = Map.of(
                cityRepository.getCityByName(CityName.VALLADOLID),
                List.of()
        );
        when(connectionManagement.getAvailableDestinations(LOBBY_CODE, 5)).thenReturn(availableDestinations);

        ICity startCity = cityRepository.getCityByName(CityName.EVORA);
        ICity destinationCity = cityRepository.getCityByName(CityName.VALLADOLID);
        IUser user = new User("user1");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
                             .get(0);
        setupPlayerForMove(startCity, player, new Nurse(), new ArrayList<>());
        buildTrainTracks();

        gameManagement.movePlayer(user, "lobbyCode", destinationCity, null);

        assertEquals(destinationCity, player.getCurrentPosition(), "Expected player to have moved to Valladolid");
    }

    private void createTestPlayers(IUser... users) {
        List<IPlayer> players = new ArrayList<>();
        for (IUser user : users) {
            IPlayer player = new Player(user);
            players.add(player);
        }
        when(game.getPlayers()).thenReturn(players);
        IGameState state = new PlayerTurnState();
        when(game.getState()).thenReturn(state);
    }

    private void setupPlayerForMove(
            ICity startCity, IPlayer player, IRole role, List<ICard> cards
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
        List<GameActions> actions = gameManagement.getAvailableActions(LOBBY_CODE, null);
        assertEquals(6, actions.size());
    }

    @Test
    void buildTrainTrack_Successful() throws GameManagementException {
        IConnection connection = new Connection(1, List.of(ALICANTE, ALBACETE), true);
        IUser user = mock(IUser.class);
        IPlayer player = mock(IPlayer.class);
        ICity city = mock(ICity.class);

        when(game.getState()).thenReturn(new PlayerTurnState());
        when(game.getCurrentPlayer()).thenReturn(player);
        when(game.getConnectionRepository()).thenReturn(new ConnectionRepository());
        when(player.getRole()).thenReturn(new Nurse());
        when(player.getUser()).thenReturn(user);
        when(player.getCurrentPosition()).thenReturn(city);
        when(city.getId()).thenReturn(1);
        when(connectionManagement.getBuildableTrainTracks(LOBBY_CODE, 1)).thenReturn(
                List.of(connection)
        );

        gameManagement.buildTrainTrack(user, LOBBY_CODE, connection);

        assertTrue(game.getConnectionRepository()
                       .getConnectionByID(connection.getId())
                       .isTrainTrack());
    }

    @Test
    void buildTrainTrack_GameNotInPlayerTurnState_ThrowsException() {
        IConnection connection = new Connection(1, List.of(ALICANTE, ALBACETE), true);
        IUser user = mock(IUser.class);

        when(game.getState()).thenReturn(new WaitForPositioning());

        GameManagementException exception = assertThrows(
                GameManagementException.class,
                () -> gameManagement.buildTrainTrack(user, LOBBY_CODE, connection)
        );

        assertEquals("Game is not in a state that allows to build train tracks", exception.getMessage());
    }

    @Test
    void buildTrainTrack_UserNotCurrentPlayer_ThrowsException() {
        IConnection connection = new Connection(1, List.of(ALICANTE, ALBACETE), true);
        IUser user = new User("test", "test");
        IPlayer player = mock(IPlayer.class);

        when(game.getState()).thenReturn(new PlayerTurnState());
        when(player.getUser()).thenReturn(user);
        when(game.getCurrentPlayer()).thenReturn(player);

        GameManagementException exception = assertThrows(
                GameManagementException.class,
                () -> gameManagement.buildTrainTrack(new User("test2", "test2"), LOBBY_CODE, connection)
        );

        assertEquals("Player is not the current player", exception.getMessage());
    }

    @Test
    void buildTrainTrack_ConnectionNotBuildable_ThrowsException() {
        IConnection connection = new Connection(1, List.of(ALICANTE, ALBACETE), true);
        IUser user = mock(IUser.class);
        IPlayer player = mock(IPlayer.class);
        ICity city = mock(ICity.class);

        when(game.getState()).thenReturn(new PlayerTurnState());
        when(game.getCurrentPlayer()).thenReturn(player);
        when(game.getConnectionRepository()).thenReturn(new ConnectionRepository());
        when(player.getRole()).thenReturn(new Nurse());
        when(player.getUser()).thenReturn(user);
        when(player.getCurrentPosition()).thenReturn(city);
        when(city.getId()).thenReturn(1);
        when(connectionManagement.getBuildableTrainTracks(LOBBY_CODE, 1)).thenReturn(
                List.of(new Connection(2, List.of(BARCELONA, TERUEL), false))
        );

        GameManagementException exception = assertThrows(
                GameManagementException.class,
                () -> gameManagement.buildTrainTrack(user, LOBBY_CODE, connection)
        );

        assertEquals("Connection between " + connection.getCityNames()
                                                       .get(0) + " and " + connection.getCityNames()
                                                                                     .get(1) + " is not buildable"
                , exception.getMessage());
    }

    @Test
    void buildTrainTrack_RailwayPersonGetsExtraBuild() throws GameManagementException {
        IConnection connection = new Connection(1, List.of(ALICANTE, ALBACETE), true);
        IUser user = mock(IUser.class);
        IPlayer player = mock(IPlayer.class);
        ICity city = mock(ICity.class);
        IGameState state = new PlayerTurnState();

        when(game.getState()).thenReturn(state);
        when(game.getCurrentPlayer()).thenReturn(player);
        when(game.getConnectionRepository()).thenReturn(new ConnectionRepository());
        when(player.getRole()).thenReturn(new RailwayWorker());
        when(player.getUser()).thenReturn(user);
        when(player.getCurrentPosition()).thenReturn(city);
        when(city.getId()).thenReturn(1);
        when(city.getName()).thenReturn(ALICANTE);
        when(connectionManagement.getBuildableTrainTracks(LOBBY_CODE, 1)).thenReturn(
                List.of(connection)
        );

        gameManagement.buildTrainTrack(user, LOBBY_CODE, connection);

        assertTrue(game.getConnectionRepository()
                       .getConnectionByID(connection.getId())
                       .isTrainTrack());

        verify(game, times(1)).setState(any(BuildExtraTrainTrackState.class));
    }

    @Test
    void buildTrainTrack_RailwayPersonUsesExtraBuild() throws GameManagementException {
        IConnection connection = new Connection(1, List.of(ALICANTE, ALBACETE), true);
        IUser user = mock(IUser.class);
        IPlayer player = mock(IPlayer.class);
        ICity city = mock(ICity.class);

        when(game.getState()).thenReturn(new BuildExtraTrainTrackState(List.of(connection)));
        when(game.getCurrentPlayer()).thenReturn(player);
        when(game.getConnectionRepository()).thenReturn(new ConnectionRepository());
        when(game.getPreviousState()).thenReturn(new PlayerTurnState());
        when(player.getRole()).thenReturn(new RailwayWorker());
        when(player.getUser()).thenReturn(user);
        when(player.getCurrentPosition()).thenReturn(city);
        when(city.getId()).thenReturn(1);
        when(city.getName()).thenReturn(ALICANTE);
        when(connectionManagement.getBuildableTrainTracks(LOBBY_CODE, 1)).thenReturn(
                List.of(connection)
        );

        gameManagement.buildTrainTrack(user, LOBBY_CODE, connection);

        assertTrue(game.getConnectionRepository()
                       .getConnectionByID(connection.getId())
                       .isTrainTrack());

        verify(game, times(1)).setState(any(PlayerTurnState.class));
    }

    /**
     * Tests the lockGameInWaitForConfirmation method.
     * Ensures that the game state is correctly set to WAIT_FOR_CONFIRMATION_STATE.
     */
    @Test
    void testLockGameInWaitForConfirmation() {
        IGame testGame = new Game(1, "testLobby");
        GameStore.getInstance()
                 .addGame("testLobby", testGame);
        testGame.setState(new PlayerTurnState());

        gameManagement.lockGameInWaitForConfirmation("testLobby");

        assertEquals(StateType.WAIT_FOR_CONFIRMATION_STATE,
                testGame.getState()
                        .getStateType()
        );
    }

    /**
     * Tests the unlockGameInWaitForConfirmation method.
     * Ensures that the game state is correctly set back to PLAYER_TURN_STATE.
     */
    @Test
    void testUnlockGameInWaitForConfirmation() {
        IGame testGame = new Game(1, "testLobby");
        GameStore.getInstance()
                 .addGame("testLobby", testGame);
        testGame.setState(new PlayerTurnState());
        testGame.setState(new WaitForConfirmationState());

        gameManagement.unlockGameInWaitForConfirmation("testLobby");

        assertEquals(StateType.PLAYER_TURN_STATE,
                testGame.getState()
                        .getStateType()
        );
    }

    /**
     * Tests the movePlayer method with a game in the event state after the OnTheMoveDayAndNightEventCard has been
     * thrown.
     *
     * @throws GameManagementException
     */
    @Test
    void testMovePlayer_OnTheMoveDayAndNightEvent() throws GameManagementException {
        Map<ICity, List<ICard>> availableDestinations = Map.of(cityRepository.getCityByName(CityName.PALMA_DE_MALLORCA),
                List.of()
        );
        when(connectionManagement.getAllDestinations(LOBBY_CODE)).thenReturn(availableDestinations);
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.PALMA_DE_MALLORCA);
        IUser user = new User("user1", "test");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
                             .get(0);
        setupPlayerForMove(startCity, player, new Sailor(), new ArrayList<>());
        when(game.getPlayer("user1")).thenReturn(player);
        when(game.getState()).thenReturn(new EventState(new OnTheMoveDayAndNightEventCard(1)));

        gameManagement.movePlayer(user, "lobbyCode", destinationCity, null);

        assertEquals(destinationCity,
                player.getCurrentPosition(),
                "Expected player to have moved to Palma de Mallorca"
        );
    }
}
