package de.uol.swp.server.game.management;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.connection.dto.DestinationInfo;
import de.uol.swp.common.game.GameActions;
import de.uol.swp.common.game.StateType;
import de.uol.swp.common.game.TransportMode;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.cards.CardMapper;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.cards.data.eventcards.OnTheMoveDayAndNightEventCard;
import de.uol.swp.server.cards.data.eventcards.StateMobilizationEventCard;
import de.uol.swp.server.cards.management.CardNotFoundException;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.city.management.CityManagement;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.connection.data.Connection;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.connection.management.IConnectionManagement;
import de.uol.swp.server.game.GameService;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.GameInitializationException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.states.*;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.plague.management.IPlagueManagement;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.player.management.PlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.region.management.IRegionManagement;
import de.uol.swp.server.role.*;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Mock
    private IPlagueManagement plagueManagement;

    @InjectMocks
    private GameManagement gameManagement;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private IRegionManagement regionManagement;

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
    void testCreateAndInitializeGame() throws GameInitializationException {
        List<IUserDTO> testUsers = List.of(new UserDTO("test", "test"), new UserDTO("test2", "test2"));
        CreateGameRequest request = new CreateGameRequest("lobby123", 1, testUsers);

        IGame createdGame = gameManagement.createAndInitializeGame(request);

        assertEquals(
                createdGame,
                GameStore.getInstance()
                         .getGame("lobby123")
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
    void testSetPositioning_AllPlayersPositioned() throws IllegalGameStateException, GameException {
        WaitForPositioning mockState = mock(WaitForPositioning.class);
        when(mockState.getPositionedPlayersCount()).thenReturn(1);
        when(game.getState()).thenReturn(mockState);

        IUser testUser = new User("test", "test");
        Session session = UUIDSession.create(testUser);
        IPlayer player = new Player(testUser, "gameId");
        when(game.getPlayers()).thenReturn(List.of(player));
        when(game.getCurrentPlayer()).thenReturn(player);

        ICity albacete = cityRepository.getCityByName(ALBACETE);
        CityCard cityCard = new CityCard(
                albacete.getId(),
                albacete.getName()
                        .toString(),
                albacete
        );
        playerManagement.addCard(
                game.getGameId(),
                player.getUser()
                      .getUsername(),
                cityCard
        );

        PositioningRequest request = new PositioningRequest(LOBBY_CODE, 34);
        request.setSession(session);

        when(game.getCityRepository()).thenReturn(cityRepository);

        gameManagement.setPositioning(request);

        verify(game).setState(any(PlayerTurnState.class));
        verify(game).setCurrentPlayerIndex(0);
    }

    @Test
    void testSetPositioning_SessionNotFound() throws PlayerManagementException {
        WaitForPositioning mockState = mock(WaitForPositioning.class);
        when(mockState.getPositionedPlayersCount()).thenReturn(1);
        when(game.getState()).thenReturn(mockState);

        IUser testUser = new User("test", "test");
        IPlayer player = new Player(testUser, "gameId");
        when(game.getPlayers()).thenReturn(List.of(player));
        when(game.getGameId()).thenReturn("gameId");
        doThrow(PlayerManagementException.class).when(playerManagement)
                                                .setStartingPosition("gameId", ALBACETE, player);

        PositioningRequest request = new PositioningRequest(LOBBY_CODE, 34);

        assertThrows(SessionNotFoundException.class, () -> gameManagement.setPositioning(request));
    }

    @Test
    void testSetPositioning_PlayerAlreadyPositioned() throws PlayerManagementException {
        PositioningRequest request = new PositioningRequest(LOBBY_CODE, 12);
        IUser testUser = new User("test", "test");
        Session session = UUIDSession.create(testUser);
        request.setSession(session);

        IPlayer player = new Player(testUser, "gameId");
        player.setCurrentPosition(cityRepository.getCityByName(CityName.BARCELONA));
        when(game.getPlayers()).thenReturn(List.of(player));
        when(game.getState()).thenReturn(mock(WaitForPositioning.class));

        verify(playerManagement, never()).setStartingPosition(LOBBY_CODE, CityName.BARCELONA, player);
    }

    @Test
    void testSetPositioning_PlayerManagementException() throws PlayerManagementException {
        PositioningRequest request = new PositioningRequest(LOBBY_CODE, 12);
        IUser testUser = new User("test", "test");
        Session session = UUIDSession.create(testUser);
        request.setSession(session);

        IPlayer player = new Player(testUser, "gameId");
        when(game.getCityRepository()).thenReturn(cityRepository);
        when(game.getPlayers()).thenReturn(List.of(player));
        when(game.getState()).thenReturn(mock(WaitForPositioning.class));
        doThrow(PlayerManagementException.class).when(playerManagement)
                                                .setStartingPosition(
                                                        anyString(),
                                                        any(CityName.class),
                                                        any(Player.class)
                                                );

        GameException exception = assertThrows(GameException.class, () -> gameManagement.setPositioning(request));

        assertEquals("Failed to set Position", exception.getMessage());
    }

    @Test
    void testDrawInfectionCard_InfectionState() {
        InfectionCard infectionCard = new InfectionCard(
                1,
                "InfectionCard",
                cityRepository.getCityByName(CityName.BARCELONA)
        );
        when(game.getInfectionCardDrawPile()).thenReturn(new ArrayList<>(List.of(infectionCard)));
        when(game.getState()).thenReturn(new InfectionState());

        InfectionCard drawnCard = gameManagement.drawInfectionCard(game);

        assertNull(drawnCard, "Expected no card to be returned in InfectionState");
        verify(cityManagement, times(1)).infectCityWithOwnPlague(game, infectionCard, 1);
    }

    @Test
    void testDrawInfectionCard_InfectionState_FavorableTimeEventCardPlayed() {
        InfectionCard infectionCard = new InfectionCard(
                1,
                "InfectionCard",
                cityRepository.getCityByName(CityName.BARCELONA)
        );
        when(game.isFavorableTimeEventCardPlayed()).thenReturn(true);
        when(game.getInfectionCardDrawPile()).thenReturn(new ArrayList<>(List.of(infectionCard)));
        when(game.getState()).thenReturn(new InfectionState());

        InfectionCard drawnCard = gameManagement.drawInfectionCard(game);

        assertNull(drawnCard, "Expected no card to be returned in InfectionState");
        verify(cityManagement, times(1)).infectCityWithOwnPlague(game, infectionCard, 1);
        verify(game, times(1)).setFavorableTimeEventCardPlayed(false);
    }

    @Test
    void testDrawInfectionCard_InvalidState() {
        InfectionCard infectionCard = new InfectionCard(
                1,
                "InfectionCard",
                cityRepository.getCityByName(CityName.BARCELONA)
        );
        when(game.getInfectionCardDrawPile()).thenReturn(new ArrayList<>(List.of(infectionCard)));
        when(game.getState()).thenReturn(mock(PlayerTurnState.class));

        InfectionCard drawnCard = gameManagement.drawInfectionCard(game);

        assertNull(drawnCard, "Expected no card to be returned in an invalid state");
    }

    @Test
    void testDrawInfectionCardWithEmptyInfectionDeck() {
        when(game.getInfectionCardDrawPile()).thenReturn(new ArrayList<>());

        assertThrows(IllegalStateException.class, () -> gameManagement.drawInfectionCard(game));
    }

    @Test
    void testGameManagementException() {
        GameManagementException exception = assertThrows(
                GameManagementException.class, () -> {
                    throw new GameManagementException("Test Exception");
                }
        );

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

        assertThrows(
                GameException.class,
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

        assertThrows(
                IllegalGameStateException.class,
                () -> gameManagement.movePlayer(user, "lobbyCode", destinationCity, null),
                "Expected GameManagementException"
        );
    }

    @Test
    void testMoveByLand() throws IllegalGameStateException, GameException {
        Map<Integer, DestinationInfo> availableDestinations = Map.of(
                cityRepository.getCityByName(CityName.PALMA_DE_MALLORCA)
                              .getId(), new DestinationInfo(List.of(), new ArrayList<>(List.of(TransportMode.CARRIAGE)))
        );
        when(connectionManagement.getAvailableDestinations(LOBBY_CODE, "user1")).thenReturn(availableDestinations);
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.PALMA_DE_MALLORCA);
        IUser user = new User("user1", "password");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
                             .get(0);
        setupPlayerForMove(startCity, player, new Sailor(), new ArrayList<>());

        gameManagement.movePlayer(user, "lobbyCode", destinationCity, null);

        assertEquals(
                destinationCity,
                player.getCurrentPosition(),
                "Expected player to have moved to Palma de Mallorca"
        );
        assertEquals(
                3,
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

        assertThrows(
                GameException.class,
                () -> gameManagement.movePlayer(user, "lobbyCode", destinationCity, null),
                "Expected GameException"
        );
    }

    @Test
    void testMoveBySea() throws IllegalGameStateException, GameException {
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.ALICANTE);
        ICard destinationCityCard = new CityCard(
                destinationCity.getId(),
                destinationCity.getName()
                               .toString(),
                destinationCity
        );

        Map<Integer, DestinationInfo> availableDestinations = Map.of(
                cityRepository.getCityByName(CityName.ALICANTE)
                              .getId(), new DestinationInfo(
                        CardMapper.toMixedCardDTOList(List.of(destinationCityCard)),
                        new ArrayList<>(List.of(TransportMode.SHIP))
                )
        );
        when(connectionManagement.getAvailableDestinations(LOBBY_CODE, "user1")).thenReturn(availableDestinations);

        IUser user = new User("user1", "");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
                             .get(0);
        setupPlayerForMove(startCity, player, new Nurse(), new ArrayList<>(List.of(destinationCityCard)));

        assertTrue(
                player.getCards()
                      .contains(destinationCityCard), "Expected player to have the destination city card"
        );

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

        assertThrows(
                GameException.class,
                () -> gameManagement.movePlayer(user, "lobbyCode", destinationCity, null),
                "Expected GameManagementException"
        );
    }

    @Test
    void testMoveSailorBySea() throws IllegalGameStateException, GameException {
        Map<Integer, DestinationInfo> availableDestinations = Map.of(
                cityRepository.getCityByName(CityName.ALICANTE)
                              .getId(), new DestinationInfo(List.of(), new ArrayList<>(List.of(TransportMode.SHIP)))
        );
        when(connectionManagement.getAvailableDestinations(LOBBY_CODE, "user1")).thenReturn(availableDestinations);
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.ALICANTE);
        IUser user = new User("user1", "password");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
                             .get(0);
        setupPlayerForMove(startCity, player, new Sailor(), new ArrayList<>());

        gameManagement.movePlayer(user, "lobbyCode", destinationCity, null);

        assertEquals(destinationCity, player.getCurrentPosition(), "Expected player to have moved to Alicante");
    }

    @Test
    void testMoveSailorBySeaWithCityCard() throws IllegalGameStateException, GameException {
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.ALICANTE);
        ICard destinationCityCard = new CityCard(
                destinationCity.getId(),
                destinationCity.getName()
                               .toString(),
                destinationCity
        );

        Map<Integer, DestinationInfo> availableDestinations = Map.of(
                cityRepository.getCityByName(CityName.ALICANTE)
                              .getId(), new DestinationInfo(
                        CardMapper.toMixedCardDTOList(List.of(destinationCityCard)),
                        new ArrayList<>(List.of(TransportMode.SHIP))
                )
        );
        when(connectionManagement.getAvailableDestinations(LOBBY_CODE, "user1")).thenReturn(availableDestinations);

        IUser user = new User("user1", "password");
        createTestPlayers(user);
        IPlayer player = game.getPlayers()
                             .get(0);
        setupPlayerForMove(startCity, player, new Sailor(), new ArrayList<>(List.of(destinationCityCard)));
        when(game.getPlayer("user1")).thenReturn(player);

        assertTrue(
                player.getCards()
                      .contains(destinationCityCard), "Expected player to have the destination city card"
        );

        gameManagement.movePlayer(user, "lobbyCode", destinationCity, destinationCityCard);

        assertEquals(destinationCity, player.getCurrentPosition(), "Expected player to have moved to Alicante");
        assertTrue(
                player.getCards()
                      .contains(destinationCityCard),
                "Expected player to have not discarded the destination city card, because he is the sailor"
        );
    }

    @Test
    void testTrainRide() throws IllegalGameStateException, GameException {
        Map<Integer, DestinationInfo> availableDestinations = Map.of(
                cityRepository.getCityByName(CityName.VALLADOLID)
                              .getId(), new DestinationInfo(List.of(), new ArrayList<>(List.of(TransportMode.TRAIN)))
        );
        when(connectionManagement.getAvailableDestinations(LOBBY_CODE, "user1")).thenReturn(availableDestinations);

        ICity startCity = cityRepository.getCityByName(CityName.EVORA);
        ICity destinationCity = cityRepository.getCityByName(CityName.VALLADOLID);
        IUser user = new User("user1", "password");
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
            IPlayer player = new Player(user, "gameId");
            players.add(player);
        }
        when(game.getPlayers()).thenReturn(players);
        IGameState state = new PlayerTurnState();
        when(game.getState()).thenReturn(state);
    }

    private void setupPlayerForMove(ICity startCity, IPlayer player, IRole role, List<ICard> cards) {
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
        IUser user = mock(IUser.class);
        when(user.getUsername()).thenReturn("username");
        when(cityManagement.isHospitalBuildable(any(String.class), any(String.class))).thenReturn(true);

        String lobbyCode = "testLobby";

        game = mock(IGame.class);
        GameStore.getInstance()
                 .addGame(lobbyCode, game);

        IPlayer player = mock(IPlayer.class);
        ICity city = mock(ICity.class);
        CityCard cityCard = mock(CityCard.class);

        when(game.getCurrentPlayer()).thenReturn(player);
        when(player.getCurrentPosition()).thenReturn(city);
        when(city.getId()).thenReturn(1);
        when(game.getPlayers()).thenReturn(List.of(player));
        when(player.getCards()).thenReturn(List.of(cityCard));
        when(cityCard.getCity()).thenReturn(city);
        when(player.getRole()).thenReturn(new Politician());
        when(player.getUser()).thenReturn(user);
        when(connectionManagement.getBuildableTrainTracks("testLobby", 1)).thenReturn(List.of(new Connection(1,
                List.of(ALICANTE, ALBACETE),
                true
        )));

        when(game.getState()).thenReturn(new PlayerTurnState());
        List<GameActions> actions = gameManagement.getAvailableActions(lobbyCode, user);

        assertEquals(6, actions.size());
    }

    @Test
    void testGetAvailableActionsScientist() {
        IUser user = mock(IUser.class);
        when(user.getUsername()).thenReturn("username");
        when(cityManagement.isHospitalBuildable(any(String.class), any(String.class))).thenReturn(true);

        String lobbyCode = "testLobby";

        game = mock(IGame.class);
        GameStore.getInstance()
                 .addGame(lobbyCode, game);

        IPlayer player = mock(IPlayer.class);
        ICity city = mock(ICity.class);
        CityCard cityCard = mock(CityCard.class);

        when(game.getCurrentPlayer()).thenReturn(player);
        when(player.getCurrentPosition()).thenReturn(city);
        when(city.getId()).thenReturn(1);
        when(game.getPlayers()).thenReturn(List.of(player));
        when(game.getPlayerCardDrawPile()).thenReturn(List.of(mock(ICard.class)));
        when(player.getCards()).thenReturn(List.of(cityCard));
        when(cityCard.getCity()).thenReturn(city);
        when(player.getRole()).thenReturn(new ScientistAtTheRoyalAcademy());
        when(player.getUser()).thenReturn(user);

        when(game.getState()).thenReturn(new PlayerTurnState());
        List<GameActions> actions = gameManagement.getAvailableActions(lobbyCode, user);

        assertEquals(7, actions.size());
    }



    @Test
    void buildTrainTrack_Successful() throws IllegalGameStateException, GameException {
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
        when(connectionManagement.getBuildableTrainTracks(LOBBY_CODE, 1)).thenReturn(List.of(connection));

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

        IllegalGameStateException exception = assertThrows(
                IllegalGameStateException.class,
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

        GameException exception = assertThrows(
                GameException.class,
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
        when(connectionManagement.getBuildableTrainTracks(LOBBY_CODE, 1)).thenReturn(List.of(new Connection(
                2,
                List.of(BARCELONA, TERUEL),
                false
        )));

        GameException exception = assertThrows(
                GameException.class,
                () -> gameManagement.buildTrainTrack(user, LOBBY_CODE, connection)
        );

        assertEquals(
                "Connection between " + connection.getCityNames()
                                                  .get(0) + " and " + connection.getCityNames()
                                                                                .get(1) + " is not buildable",
                exception.getMessage()
        );
    }

    @Test
    void buildTrainTrack_RailwayPersonGetsExtraBuild() throws IllegalGameStateException, GameException {
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
        when(connectionManagement.getBuildableTrainTracks(LOBBY_CODE, 1)).thenReturn(List.of(connection));

        gameManagement.buildTrainTrack(user, LOBBY_CODE, connection);

        assertTrue(game.getConnectionRepository()
                       .getConnectionByID(connection.getId())
                       .isTrainTrack());

        verify(game, times(1)).setState(any(BuildExtraTrainTrackState.class));
    }

    @Test
    void buildTrainTrack_RailwayPersonUsesExtraBuild() throws IllegalGameStateException, GameException {
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
        when(connectionManagement.getBuildableTrainTracks(LOBBY_CODE, 1)).thenReturn(List.of(connection));

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

        assertEquals(
                StateType.WAIT_FOR_CONFIRMATION_STATE,
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

        assertEquals(
                StateType.PLAYER_TURN_STATE,
                testGame.getState()
                        .getStateType()
        );
    }

    /**
     * Tests the movePlayer method with a game in the event state after the OnTheMoveDayAndNightEventCard has been
     * thrown.
     */
    @Test
    void testMovePlayer_OnTheMoveDayAndNightEvent() throws IllegalGameStateException, GameException {
        Map<Integer, DestinationInfo> availableDestinations = Map.of(
                cityRepository.getCityByName(CityName.PALMA_DE_MALLORCA)
                              .getId(), new DestinationInfo(List.of(), new ArrayList<>(List.of(TransportMode.NONE)))
        );
        when(connectionManagement.getAllDestinations(LOBBY_CODE)).thenReturn(availableDestinations);
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.PALMA_DE_MALLORCA);
        IUser testUser = new User("user1", "test");
        createTestPlayers(testUser);
        IPlayer player = game.getPlayers()
                             .get(0);
        setupPlayerForMove(startCity, player, new Sailor(), new ArrayList<>());
        when(game.getPlayer("user1")).thenReturn(player);
        when(game.getState()).thenReturn(new EventState(new OnTheMoveDayAndNightEventCard(1)));

        gameManagement.movePlayer(testUser, "lobbyCode", destinationCity, null);

        assertEquals(
                destinationCity,
                player.getCurrentPosition(),
                "Expected player to have moved to Palma de Mallorca"
        );
    }

    @Test
    void testMovePlayer_StateMobilizationEvent() throws IllegalGameStateException, GameException {
        Map<Integer, DestinationInfo> availableDestinations = Map.of(
                cityRepository.getCityByName(CityName.PALMA_DE_MALLORCA)
                              .getId(),
                new DestinationInfo(List.of(), new ArrayList<>(List.of(TransportMode.NONE))),
                cityRepository.getCityByName(CityName.BARCELONA)
                              .getId(),
                new DestinationInfo(List.of(), new ArrayList<>(List.of(TransportMode.NONE)))
        );
        when(connectionManagement.getAvailableDestinations(anyString(), anyString())).thenReturn(availableDestinations);
        ICity startCity = cityRepository.getCityByName(CityName.BARCELONA);
        ICity destinationCity = cityRepository.getCityByName(CityName.PALMA_DE_MALLORCA);
        IUser testUser1 = new User("user1", "test");
        IUser testUser2 = new User("user2", "test");

        createTestPlayers(testUser1, testUser2);
        IPlayer player = game.getPlayers()
                             .get(0);
        setupPlayerForMove(startCity, player, new Sailor(), new ArrayList<>());
        when(game.getPlayer("user1")).thenReturn(player);
        IPlayer player2 = game.getPlayers()
                              .get(1);
        setupPlayerForMove(startCity, player2, new Sailor(), new ArrayList<>());
        when(game.getPlayer("user2")).thenReturn(player2);

        StateMobilizationEventCard stateMobilizationEventCard = new StateMobilizationEventCard(1);
        stateMobilizationEventCard.setPlayersToMove(game.getPlayers());
        when(game.getState()).thenReturn(new EventState(stateMobilizationEventCard));

        gameManagement.movePlayer(testUser1, "lobbyCode", destinationCity, null);

        assertEquals(
                destinationCity.getName(),
                player.getCurrentPosition().getName(),
                "Expected player1 to have moved to Palma de Mallorca"
        );
        assertEquals(
                1,
                stateMobilizationEventCard.getPlayersToMove()
                                          .size(),
                "Expected playersToMove to be decreased by 1"
        );

        gameManagement.movePlayer(testUser2, "lobbyCode", startCity, null);

        assertEquals(
                destinationCity,
                player.getCurrentPosition(),
                "Expected player2 to have moved to Palma de Mallorca"
        );
        assertEquals(
                0,
                stateMobilizationEventCard.getPlayersToMove()
                                          .size(),
                "Expected playersToMove to be decreased by 1"
        );
    }

    @Test
    void testCreatePlayersWithDifferentUserCounts() throws IllegalGameStateException {
        List<IUser> twoUsers = List.of(new User("user1", "pass1"), new User("user2", "pass2"));
        List<IUser> threeUsers = List.of(
                new User("user1", "pass1"),
                new User("user2", "pass2"),
                new User("user3", "pass3")
        );
        List<IUser> fourUsers = List.of(
                new User("user1", "pass1"),
                new User("user2", "pass2"),
                new User("user3", "pass3"),
                new User("user4", "pass4")
        );

        doAnswer(invocation -> {
            Player player = invocation.getArgument(1);
            player.getCards()
                  .add(mock(ICard.class));
            return null;
        }).when(playerManagement)
          .drawPlayerCard(anyString(), any(Player.class));
        when(game.getPlayers()).thenReturn(new ArrayList<>());

        gameManagement.createPlayers(twoUsers, game);
        assertEquals(
                4,
                game.getPlayers()
                    .get(0)
                    .getCards()
                    .size()
        );

        game.getPlayers()
            .clear();
        gameManagement.createPlayers(threeUsers, game);
        assertEquals(
                3,
                game.getPlayers()
                    .get(0)
                    .getCards()
                    .size()
        );

        game.getPlayers()
            .clear();
        gameManagement.createPlayers(fourUsers, game);
        assertEquals(
                2,
                game.getPlayers()
                    .get(0)
                    .getCards()
                    .size()
        );
    }

    @Test
    void testSetStartingPlayer() {
        IGame game1 = new Game(1, "testLobby");
        ICity city1 = mock(ICity.class);
        ICity city2 = mock(ICity.class);
        IPlayer player1 = new Player(new User("user1", "pass1"), "gameId");
        IPlayer player2 = new Player(new User("user2", "pass2"), "gameId");
        player1.getCards()
               .add(new CityCard(1, "City1", city1));
        player2.getCards()
               .add(new CityCard(2, "City2", city2));
        game1.getPlayers()
             .add(player1);
        game1.getPlayers()
             .add(player2);

        gameManagement.setStartingPlayer(game1);

        assertEquals(
                player1,
                game1.getPlayers()
                     .get(0)
        );
    }

    @Test
    void testIsWaterTreatmentPlaceable() {
        IUser user = new User("testUser", "testPassword");
        when(game.getWaterTreatmentsLeft()).thenReturn(1);
        when(regionManagement.getAvailableRegions(any(), anyString())).thenReturn(Set.of(mock(IRegionDTO.class)));

        boolean result = gameManagement.isWaterTreatmentPlaceable(LOBBY_CODE, user);

        assertTrue(result);
    }

    @Test
    void testDiscardInfectionCard() {
        IGame mockGame = mock(IGame.class);
        InfectionCard infectionCard = new InfectionCard(1, "InfectionCard", mock(ICity.class));
        List<InfectionCard> discardPile = new ArrayList<>();

        when(mockGame.getInfectionCardDiscardPile()).thenReturn(discardPile);

        gameManagement.discardInfectionCard(mockGame, infectionCard);

        assertTrue(discardPile.contains(infectionCard), "The infection card should be in the discard pile");
    }

    @Test
    void testIncreaseCurrentPlayerActions_CurrentState() {
        PlayerTurnState playerTurnState = mock(PlayerTurnState.class);
        when(game.getState()).thenReturn(playerTurnState);
        when(playerTurnState.getActionsRemaining()).thenReturn(5);

        gameManagement.increaseCurrentPlayerActions(game, 3);

        verify(playerTurnState).setActionsRemaining(8);
    }

    @Test
    void testIncreaseCurrentPlayerActions_PreviousState() {
        PlayerTurnState playerTurnState = mock(PlayerTurnState.class);
        when(game.getState()).thenReturn(mock(IGameState.class));
        when(game.getPreviousState()).thenReturn(playerTurnState);
        when(playerTurnState.getActionsRemaining()).thenReturn(5);

        gameManagement.increaseCurrentPlayerActions(game, 3);

        verify(playerTurnState).setActionsRemaining(8);
    }

    @Test
    void testIncreaseCurrentPlayerActions_NoPlayerTurnState() {
        PlayerTurnState playerTurnState = mock(PlayerTurnState.class);
        when(game.getState()).thenReturn(mock(IGameState.class));
        when(game.getPreviousState()).thenReturn(mock(IGameState.class));

        gameManagement.increaseCurrentPlayerActions(game, 3);

        verify(playerTurnState, never()).setActionsRemaining(anyInt());
    }


    //    /**
    //     * Tests the shareKnowledgeRequestAccepted method.
    //     * Ensures that the knowledge sharing between players is handled correctly.
    //     *
    //     * @throws PlayerManagementException if there is an error in player management
    //     */
    //    @Test
    //    void shareKnowledgeRequestAcceptedTest() throws PlayerManagementException {
    //        IGame notMockedGame = new Game(
    //                "testGame",
    //                mock(RoleRepository.class),
    //                mock(CityRepository.class),
    //                mock(RegionRepository.class),
    //                mock(ConnectionRepository.class),
    //                mock(PlagueRepository.class),
    //                mock(CardRepository.class),
    //                1,
    //                0,
    //                14,
    //                20,
    //                new ArrayList<>(),
    //                new ArrayList<>(),
    //                new ArrayList<>(),
    //                new ArrayList<>(),
    //                new ArrayList<>(),
    //                0,
    //                new PlayerTurnState(),
    //                mock(IGameState.class),
    //                1,
    //                mock(GameStateChangeListener.class)
    //        );
    //        IPlayer currentPlayer = new Player(new User("test", "test"));
    //        ICard currentPlayerCard = new CityCard(1, "test", mock(ICity.class));
    //        currentPlayer.getCards()
    //                     .add(currentPlayerCard);
    //
    //        IPlayer targetPlayer = new Player(new User("test2", "test2"));
    //        ICard targetPlayerCard = new CityCard(2, "test2", mock(ICity.class));
    //        targetPlayer.getCards()
    //                    .add(targetPlayerCard);
    //
    //        String lobbyId = "testLobby";
    //        ILobbyManagement lobbyManagement = mock(ILobbyManagement.class);
    //        GameStore.getInstance()
    //                 .addGame(lobbyId, notMockedGame);
    //
    //        when(playerManagement.getCard("testLobby", "test", 1)).thenReturn(currentPlayerCard);
    //        when(playerManagement.getCard("testLobby", "test2", 2)).thenReturn(targetPlayerCard);
    //
    //        GameService gameService = mock(GameService.class);
    //        ShareKnowledgeEvent event = new ShareKnowledgeEvent(
    //                lobbyId,
    //                currentPlayer.getUser()
    //                             .getUsername(),
    //                targetPlayer.getUser()
    //                            .getUsername(),
    //                CardMapper.toDTO(currentPlayerCard),
    //                CardMapper.toDTO(targetPlayerCard)
    //        );
    //
    //        gameManagement.shareKnowledgeRequestAccepted(currentPlayer, targetPlayer, lobbyId, event, gameService);
    //        System.out.println("Current Player Cards: " + currentPlayer.getCards() + currentPlayerCard.getTitle());
    //        System.out.println("Target Player Cards: " + targetPlayer.getCards() + targetPlayerCard.getTitle());
    //
    //        assert (currentPlayer.getCards()
    //                             .contains(targetPlayerCard));
    //        assert (targetPlayer.getCards()
    //                            .contains(currentPlayerCard));
    //        verify(gameService, times(1)).sendToAllInLobby(any(), any());
    //    }

    @Test
    void testShareKnowledgeWithDiscardPile() throws PlayerManagementException, CardNotFoundException {
        // Arrange
        int cardToDiscardID = 1;
        int cardToReceiveID = 2;
        String lobbyId = "lobbyCode";
        GameService gameService = mock(GameService.class);
        IPlayer player1 = new Player(new User("user1", "pass1"), "gameId");
        ICard discardCard = new CityCard(2, "cardToReceive", mock(ICity.class));
        ArrayList<ICard> discardPile = new ArrayList<>();
        discardPile.add(discardCard);
        when(game.getCurrentPlayer()).thenReturn(player1);
        when(game.getPlayerCardDiscardPile()).thenReturn(discardPile);
        when(game.getState()).thenReturn(new PlayerTurnState());


        gameManagement.shareKnowledgeWithDiscardPile(cardToDiscardID, cardToReceiveID, lobbyId, gameService);

        verify(gameService).sendBoardUpdateAfterCardExchangeWithDiscardPile(lobbyId);
    }

    @Test
    void testEndTurn() throws IllegalGameStateException {
        IPlayer player = mock(IPlayer.class);
        IUser user = new User("test", "test");
        when(player.getUser()).thenReturn(user);
        when(game.getCurrentPlayer()).thenReturn(player);
        when(game.getState()).thenReturn(new PlayerTurnState());

        gameManagement.endTurn(game.getGameId(), user);

        verify(game).setState(any(DrawCardState.class));
    }

    @Test
    void testEndTurn_IllegalGameStateException() {
        IPlayer player = mock(IPlayer.class);
        IUser user = new User("test", "test");
        when(player.getUser()).thenReturn(user);
        when(game.getCurrentPlayer()).thenReturn(player);
        when(game.getState()).thenReturn(new DrawCardState());

        assertThrows(IllegalGameStateException.class, () -> gameManagement.endTurn(game.getGameId(), user));
    }
}
