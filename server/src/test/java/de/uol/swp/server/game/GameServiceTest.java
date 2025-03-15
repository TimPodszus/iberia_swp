package de.uol.swp.server.game;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.connection.response.BuildableTrainTracksResponse;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.event.CardExchangeConfirmationEvent;
import de.uol.swp.common.game.message.event.SwapCardsConfirmationEvent;
import de.uol.swp.common.game.message.request.*;
import de.uol.swp.common.game.message.response.AvailableActionsResponse;
import de.uol.swp.common.game.message.response.CreateGameResponse;
import de.uol.swp.common.game.message.response.PoliticianSecondRoleActionResponse;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.lobby.message.event.LobbyClosedEvent;
import de.uol.swp.common.player.message.request.MovePlayerRequest;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.events.AnotherDayEvent;
import de.uol.swp.server.cards.events.FavorableTimeEvent;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.connection.data.Connection;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.connection.management.IConnectionManagement;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.GameInitializationException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.exceptions.LobbyIsEmptyException;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.states.BuildExtraTrainTrackState;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.states.EndGameState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.exceptions.LobbyNotFoundException;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.plague.data.PlagueRepository;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.region.RegionRepository;
import de.uol.swp.server.role.CountryDoctor;
import de.uol.swp.server.role.Politician;
import de.uol.swp.server.role.Sailor;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for GameService.
 */
public class GameServiceTest extends EventBusBasedTest {
    private static final String LOBBY_CODE = "lobby123";
    private static final int GAME_ID = 1;

    @Mock
    private IGameManagement gameManagement;

    @Mock
    private ICityManagement cityManagement;

    @Mock
    private ILobbyManagement lobbyManagement;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private IPlayerManagement playerManagement;

    @Mock
    private IConnectionManagement connectionManagement;

    @Mock
    IGame game;

    @Spy
    @InjectMocks
    GameService gameService = new GameService(
            getBus(),
            lobbyManagement,
            gameManagement,
            cityManagement,
            playerManagement,
            connectionManagement
    );

    /**
     * Handles BoardUpdateEvent.
     * Handles AvailableActionsResponse events.
     *
     * @param event the BoardUpdateEvent
     */
    @Subscribe
    public void onBoardUpdateEvent(BoardUpdateEvent event) {
        handleEvent(event);
    }

    /**
     * Handles CreateGameResponse.
     *
     * @param response the CreateGameResponse
     */
    @Subscribe
    public void onCreateGameResponse(CreateGameResponse response) {
        handleEvent(response);
    }

    /**
     * Handles AvailableActionsResponse events.
     *
     * @param event the AvailableActionsResponse
     */
    @Subscribe
    public void onEvent(AvailableActionsResponse event) {
        super.handleEvent(event);
    }

    /**
     * Handles ShareRideRequest events.
     *
     * @param event the ShareRideRequest
     */
    @Subscribe
    public void onShareRideEvent(ShareRideRequest event) {
        super.handleEvent(event);
    }

    @Subscribe
    public void onAnotherDayEvent(AnotherDayEvent event) {
        super.handleEvent(event);
    }

    @Subscribe
    public void onStatusResponse(StatusResponse event) {
        super.handleEvent(event);
    }

    @Subscribe
    public void onEndTurnRequest(EndTurnRequest event) {
        super.handleEvent(event);
    }

    @Subscribe
    public void onLobbyClosedEvent(LobbyClosedEvent event) {
        super.handleEvent(event);
    }

    /**
     * Sets up the test environment.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the onMovePlayerRequest method with valid inputs.
     *
     * @throws InterruptedException      if the thread is interrupted
     * @throws IllegalGameStateException if the game is in an illegal state
     * @throws GameException             if the player cannot be moved
     */
    @Test
    void testOnMovePlayerRequest() throws InterruptedException, IllegalGameStateException, GameException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));

        IUser user2 = new User("testuser2", "testpassword2");
        Session session2 = UUIDSession.create(user2);
        when(authenticationService.getSession(user2)).thenReturn(Optional.of(session2));

        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("lobbycode", 12, "testuser2");
        movePlayerRequest.setSession(session);

        ICity city = new CityRepository().getCity(12);
        when(cityManagement.getCity("lobbycode", 12)).thenReturn(city);
        IGame testGame = new Game(2, "lobbycode");
        when(gameManagement.getGame("lobbycode")).thenReturn(testGame);
        ILobby lobby = new Lobby("lobbycode", "Test", List.of(user, user2), user, 4);
        when(lobbyManagement.getLobby("lobbycode")).thenReturn(lobby);

        postAndWait(movePlayerRequest);

        verify(gameManagement, atLeast(1)).movePlayer(user, "lobbycode", city, null);
        verify(gameManagement, atLeast(1)).lockGameInWaitForConfirmation("lobbycode");
        assertInstanceOf(BoardUpdateEvent.class, event);
    }

    /**
     * Tests the onMovePlayerRequest method with an unknown player to take with.
     */
    @Test
    void testOnMovePlayerRequestWithUnknownPlayerToTakeWith() throws IllegalGameStateException, GameException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));

        IUser user2 = new User("testuser2", "testpassword2");

        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("lobbycode", 12, "testuser2");
        movePlayerRequest.setSession(session);

        ICity city = new CityRepository().getCity(12);
        when(cityManagement.getCity("lobbycode", 12)).thenReturn(city);
        IGame testGame = new Game(2, "lobbycode");
        when(gameManagement.getGame("lobbycode")).thenReturn(testGame);
        ILobby lobby = new Lobby("lobbycode", "Test", List.of(user, user2), user, 4);
        when(lobbyManagement.getLobby("lobbycode")).thenReturn(lobby);

        assertThrows(SessionNotFoundException.class, () -> gameService.onMovePlayerRequest(movePlayerRequest));

        verify(gameManagement, atLeast(1)).movePlayer(user, "lobbycode", city, null);
    }

    /**
     * Tests the onMovePlayerRequest method with a not logged-in player to take with.
     */
    @Test
    void testOnMovePlayerRequestWithNotLoggedInPlayerToTakeWith() throws IllegalGameStateException, GameException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));

        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("lobbycode", 12, "testuser2");
        movePlayerRequest.setSession(session);

        ICity city = new CityRepository().getCity(12);
        when(cityManagement.getCity("lobbycode", 12)).thenReturn(city);
        IGame testGame = new Game(2, "lobbycode");
        when(gameManagement.getGame("lobbycode")).thenReturn(testGame);
        ILobby lobby = new Lobby("lobbycode", "Test", List.of(user), user, 4);
        when(lobbyManagement.getLobby("lobbycode")).thenReturn(lobby);

        assertThrows(SessionNotFoundException.class, () -> gameService.onMovePlayerRequest(movePlayerRequest));

        verify(gameManagement, atLeast(1)).movePlayer(user, "lobbycode", city, null);
    }

    /**
     * Tests the onMovePlayerRequest method with an unknown user.
     */
    @Test
    void testOnMovePlayerRequestWithUnknownUser() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("lobbycode", 12, 1);
        movePlayerRequest.setSession(null);

        assertThrows(SessionNotFoundException.class, () -> gameService.onMovePlayerRequest(movePlayerRequest));
    }

    /**
     * Tests the onMovePlayerRequest method when the game is in an illegal state.
     *
     * @throws InterruptedException      if the thread is interrupted
     * @throws IllegalGameStateException if the game is in an illegal state
     * @throws GameException             if there is an error in the game logic
     */
    @Test
    void testOnMovePlayerRequest_IllegalGameState() throws InterruptedException, IllegalGameStateException, GameException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));

        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("lobbycode", 12);
        movePlayerRequest.setSession(session);

        ICity city = new CityRepository().getCity(12);
        when(cityManagement.getCity("lobbycode", 12)).thenReturn(city);
        doThrow(IllegalGameStateException.class).when(gameManagement)
                                                .movePlayer(user, "lobbycode", city, null);

        postAndWait(movePlayerRequest);

        assertInstanceOf(StatusResponse.class, event);
        assertFalse(((StatusResponse) event).isSuccess());
    }

    /**
     * Tests the onMovePlayerRequest method when a PlayerManagementException is thrown.
     *
     * @throws InterruptedException      if the thread is interrupted
     * @throws PlayerManagementException if there is an error in player management
     */
    @Test
    void testOnMovePlayerRequest_PlayerManagementException() throws InterruptedException, PlayerManagementException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));

        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("lobbycode", 12);
        movePlayerRequest.setSession(session);

        ICity city = new CityRepository().getCity(12);
        when(cityManagement.getCity("lobbycode", 12)).thenReturn(city);
        when(playerManagement.getCard(anyString(), anyString(), anyInt())).thenThrow(PlayerManagementException.class);

        postAndWait(movePlayerRequest);

        assertInstanceOf(StatusResponse.class, event);
        assertFalse(((StatusResponse) event).isSuccess());
    }

    /**
     * Tests the onMovePlayerRequest method when a GameException is thrown.
     *
     * @throws InterruptedException      if the thread is interrupted
     * @throws IllegalGameStateException if the game is in an illegal state
     * @throws GameException             if there is an error in the game logic
     */
    @Test
    void testOnMovePlayerRequest_GameException() throws InterruptedException, IllegalGameStateException, GameException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));

        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("lobbycode", 12);
        movePlayerRequest.setSession(session);

        ICity city = new CityRepository().getCity(12);
        when(cityManagement.getCity("lobbycode", 12)).thenReturn(city);
        IGame testGame = new Game(2, "lobbycode");
        when(gameManagement.getGame("lobbycode")).thenReturn(testGame);
        ILobby lobby = new Lobby("lobbycode", "Test", List.of(user), user, 4);
        when(lobbyManagement.getLobby("lobbycode")).thenReturn(lobby);
        doThrow(GameException.class).when(gameManagement)
                                    .movePlayer(user, "lobbycode", city, null);

        postAndWait(movePlayerRequest);

        assertInstanceOf(StatusResponse.class, event);
        assertFalse(((StatusResponse) event).isSuccess());
    }


    /**
     * Tests the onAvailableActionsRequest method.
     *
     * @throws InterruptedException if the thread is interrupted
     */
    @Test
    void testOnAvailableActionsRequest() throws InterruptedException {
        IUser user = new User("username", "password");
        Session session = UUIDSession.create(user);

        AvailableActionsRequest request = new AvailableActionsRequest("lobbyId");
        request.setSession(session);

        super.postAndWait(request);

        verify(gameManagement, atLeast(1)).getAvailableActions("lobbyId", user);
        assertInstanceOf(AvailableActionsResponse.class, event);
    }

    /**
     * Tests setting player positioning when the game is not null.
     *
     * @throws GameInitializationException if the game cannot be initialized
     * @throws InterruptedException        if the thread is interrupted
     */
    @Test
    void testOnCreateGameRequest() throws GameInitializationException, InterruptedException {
        List<IUserDTO> users = List.of(new UserDTO("username", "password"));
        IGame testGame = new Game(1, "lobby123");
        CreateGameRequest createGameRequest = new CreateGameRequest(LOBBY_CODE, GAME_ID, users);
        when(gameManagement.createAndInitializeGame(createGameRequest)).thenReturn(testGame);
        when(lobbyManagement.getLobby(LOBBY_CODE)).thenReturn(mock(Lobby.class));

        postAndWait(createGameRequest);

        assertInstanceOf(CreateGameResponse.class, event);
        assertTrue(((CreateGameResponse) event).isSuccess());
    }

    /**
     * Tests create game when the game is null.
     *
     * @throws GameInitializationException if the game cannot be initialized
     */
    @Test
    void testOnCreateGameRequest_GameIsNull() throws GameInitializationException {
        List<IUserDTO> users = List.of(new UserDTO("username", "password"));
        CreateGameRequest createGameRequest = new CreateGameRequest(LOBBY_CODE, GAME_ID, users);
        when(gameManagement.createAndInitializeGame(createGameRequest)).thenReturn(null);
        when(lobbyManagement.getLobby(LOBBY_CODE)).thenReturn(mock(Lobby.class));

        gameService.onCreateGameRequest(createGameRequest);

        verify(gameManagement, times(1)).createAndInitializeGame(createGameRequest);
        verify(lobbyManagement, times(1)).getLobby(LOBBY_CODE);
        assertNull(event, "No event should be posted when the game creation fails.");
    }

    /**
     * Tests create game when the game initialization fails.
     *
     * @throws GameInitializationException if the game cannot be initialized
     * @throws InterruptedException        if the thread is interrupted
     */
    @Test
    void testOnCreateGameRequest_FailedInitialization() throws GameInitializationException, InterruptedException {
        List<IUserDTO> users = List.of(new UserDTO("username", "password"));
        CreateGameRequest createGameRequest = new CreateGameRequest(LOBBY_CODE, GAME_ID, users);
        when(gameManagement.createAndInitializeGame(createGameRequest)).thenThrow(GameInitializationException.class);

        postAndWait(createGameRequest);

        assertInstanceOf(CreateGameResponse.class, event);
        assertFalse(((CreateGameResponse) event).isSuccess());
    }


    @Test
    void testOnBuildTrainTrackRequest_UserIsNull() {
        BuildTrainTrackRequest request = new BuildTrainTrackRequest("lobbyId", 1);
        request.setSession(null);

        assertThrows(SessionNotFoundException.class, () -> gameService.onBuildTrainTrackRequest(request));
    }

    /**
     * Tests the onBuildTrainTrackRequest method without the extra track state.
     *
     * @throws GameException             if there is an error in the game logic
     * @throws IllegalGameStateException if the game is in an illegal state
     */
    @Test
    void testOnBuildTrainTrackRequest_WithoutExtraTrackState() throws GameException, IllegalGameStateException {
        BuildTrainTrackRequest request = new BuildTrainTrackRequest("lobbyId", 1);
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        request.setSession(session);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));
        IConnection connection = new Connection(1, List.of(CityName.ALICANTE, CityName.ALBACETE), true);
        when(connectionManagement.getConnection("lobbyId", 1)).thenReturn(connection);
        when(gameManagement.getGame("lobbyId")).thenReturn(game);
        when(game.getState()).thenReturn(new PlayerTurnState());
        when(game.getCityRepository()).thenReturn(new CityRepository());
        when(game.getRegionRepository()).thenReturn(new RegionRepository(new CityRepository()));
        when(game.getPlagueRepository()).thenReturn(new PlagueRepository());
        when(game.getConnectionRepository()).thenReturn(new ConnectionRepository());
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(new Lobby("lobbyId", "Test", List.of(user), user, 4));

        IPlayer player = mock(IPlayer.class);
        when(game.getCurrentPlayer()).thenReturn(player);
        when(player.getUser()).thenReturn(user);
        gameService.onBuildTrainTrackRequest(request);

        verify(gameManagement, atLeast(1)).buildTrainTrack(user, "lobbyId", connection);
        assertInstanceOf(BoardUpdateEvent.class, event);
    }

    /**
     * Tests the onBuildTrainTrackRequest method when the connection is null.
     * Ensures that a StatusResponse event is posted with a failure status.
     */
    @Test
    void testOnBuildTrainTrackRequest_GameException() throws IllegalGameStateException, GameException, InterruptedException {
        BuildTrainTrackRequest request = new BuildTrainTrackRequest("lobbyId", 1);
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        request.setSession(session);
        IConnection connection = new Connection(1, List.of(CityName.ALICANTE, CityName.ALBACETE), true);
        when(connectionManagement.getConnection("lobbyId", 1)).thenReturn(connection);
        doThrow(GameException.class).when(gameManagement)
                                    .buildTrainTrack(user, "lobbyId", connection);
        postAndWait(request);

        assertInstanceOf(StatusResponse.class, event);
        assertFalse(((StatusResponse) event).isSuccess());
    }

    /**
     * Tests the onBuildTrainTrackRequest method when the game is in a state that does not allow building train tracks.
     * Ensures a StatusResponse is sent correctly
     */
    @Test
    void testOnBuildTrainTrackRequest_IllegalGameState() throws IllegalGameStateException, GameException, InterruptedException {
        BuildTrainTrackRequest request = new BuildTrainTrackRequest("lobbyId", 1);
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        request.setSession(session);
        IConnection connection = new Connection(1, List.of(CityName.ALICANTE, CityName.ALBACETE), true);
        when(connectionManagement.getConnection("lobbyId", 1)).thenReturn(connection);
        doThrow(IllegalGameStateException.class).when(gameManagement)
                                                .buildTrainTrack(user, "lobbyId", connection);
        postAndWait(request);

        assertInstanceOf(StatusResponse.class, event);
        assertFalse(((StatusResponse) event).isSuccess());
    }

    /**
     * Tests the onGameStateChange method when the game state is DrawCardState.
     * Ensures that the game state is changed to EndGameState.
     */
    @Test
    void testOnGameStateChange_DrawCardState() {
        ILobby lobby = mock(ILobby.class);
        IGame testGame = mock(IGame.class);
        when(lobbyManagement.getLobby("gameId")).thenReturn(lobby);
        DrawCardState drawCardState = new DrawCardState();
        when(testGame.getState()).thenReturn(drawCardState);
        when(testGame.getPlayerCardDrawPile()).thenReturn(List.of());
        when(testGame.getGameId()).thenReturn("gameId");

        gameService.onGameStateChange(testGame);

        verify(testGame).setState(any(EndGameState.class));
    }

    @Test
    void testOnBuildTrainTrackRequest_WithExtraTrackState() throws GameException, IllegalGameStateException {
        BuildTrainTrackRequest request = new BuildTrainTrackRequest("lobbyId", 1);
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        request.setSession(session);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));
        IConnection connection = new Connection(1, List.of(CityName.ALICANTE, CityName.ALBACETE), true);
        when(connectionManagement.getConnection("lobbyId", 1)).thenReturn(connection);
        when(gameManagement.getGame("lobbyId")).thenReturn(game);
        when(game.getState()).thenReturn(new BuildExtraTrainTrackState(List.of(connection), 4));
        when(game.getCityRepository()).thenReturn(new CityRepository());
        when(game.getRegionRepository()).thenReturn(new RegionRepository(new CityRepository()));
        when(game.getPlagueRepository()).thenReturn(new PlagueRepository());
        when(game.getConnectionRepository()).thenReturn(new ConnectionRepository());
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(new Lobby("lobbyId", "Test", List.of(user), user, 4));

        IPlayer player = mock(IPlayer.class);
        when(game.getCurrentPlayer()).thenReturn(player);
        when(player.getUser()).thenReturn(user);
        gameService.onBuildTrainTrackRequest(request);

        verify(gameManagement, atLeast(1)).buildTrainTrack(user, "lobbyId", connection);
        assertInstanceOf(BoardUpdateEvent.class, event);
        verify(gameService, times(1)).post(any(BuildableTrainTracksResponse.class));
    }

    @Test
    void testOnAnotherDayEvent() {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));

        AnotherDayEvent anotherDayEvent = new AnotherDayEvent("lobbycode", "testuser");
        anotherDayEvent.setSession(session);

        IGame testGame = new Game(2, "lobbycode");
        when(gameManagement.getGame("lobbycode")).thenReturn(testGame);

        ILobby lobby = new Lobby("lobbycode", "Test", List.of(user), user, 2);
        when(lobbyManagement.getLobby("lobbycode")).thenReturn(lobby);
        testGame.setState(new PlayerTurnState());

        gameService.onAnotherDayEvent(anotherDayEvent);

        verify(gameManagement, times(1)).increaseCurrentPlayerActions(testGame, 2);
        verify(gameManagement, times(1)).getGame("lobbycode");
        verify(lobbyManagement, times(1)).getLobby("lobbycode");
        verify(gameService, times(1)).sendToAllInLobby(eq(lobby), any(BoardUpdateEvent.class));
    }


    @Test
    void testOnEndTurnRequest() throws IllegalGameStateException, InterruptedException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);

        EndTurnRequest endTurnRequest = new EndTurnRequest("lobbyId");
        endTurnRequest.setSession(session);

        IGame testGame = new Game(2, "lobbyId");
        when(gameManagement.getGame("lobbyId")).thenReturn(testGame);
        ILobby lobby = new Lobby("lobbyId", "Test", List.of(user), user, 4);
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(lobby);
        testGame.setState(new PlayerTurnState());

        postAndWait(endTurnRequest);

        verify(gameManagement, times(1)).endTurn("lobbyId", user);
        assertInstanceOf(BoardUpdateEvent.class, event);
    }

    @Test
    void testOnEndTurnRequest_sessionNotFound() {
        EndTurnRequest endTurnRequest = new EndTurnRequest("lobbyId");
        assertThrows(SessionNotFoundException.class, () -> gameService.onEndTurnRequest(endTurnRequest));
    }

    @Test
    void testOnEndTurnRequest_illegalGameStateException() throws IllegalGameStateException, InterruptedException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);

        EndTurnRequest endTurnRequest = new EndTurnRequest("lobbyId");
        endTurnRequest.setSession(session);

        IGame testGame = new Game(2, "lobbyId");
        when(gameManagement.getGame("lobbyId")).thenReturn(testGame);
        ILobby lobby = new Lobby("lobbyId", "Test", List.of(user), user, 4);
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(lobby);
        doThrow(IllegalGameStateException.class).when(gameManagement)
                                                .endTurn("lobbyId", user);

        postAndWait(endTurnRequest);

        assertInstanceOf(StatusResponse.class, event);
        assertFalse(((StatusResponse) event).isSuccess());
    }

    @Test
    void testOnFavorableTimeEvent_Successful() throws GameException {
        IGame game1 = new Game(2, "lobbyId");

        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));

        IPlayer player = new Player(user, "lobbyId");
        player.setRole(new CountryDoctor());
        game1.getPlayers()
             .add(player);
        game1.setCurrentPlayerIndex(0);
        game1.setState(new PlayerTurnState());

        doNothing().when(playerManagement)
                   .discardPlayerCard("lobbyId", "testuser", 212);

        FavorableTimeEvent event = new FavorableTimeEvent("lobbyId", "testuser");
        event.setSession(session);

        when(gameManagement.getGame("lobbyId")).thenReturn(game1);
        ILobby lobby = new Lobby("lobbyId", "Test", List.of(user), user, 4);
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(lobby);

        gameService.onFavorableTimeEvent(event);

        assertTrue(game1.isFavorableTimeEventCardPlayed());
        verify(gameService).sendToAllInLobby(eq(lobby), any(BoardUpdateEvent.class));
    }


    @Test
    void testOnUserLeavedGameRequest() throws LobbyIsEmptyException, LobbyNotFoundException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));

        UserLeavedGameRequest userLeavedGameRequest = new UserLeavedGameRequest("lobbyId");
        userLeavedGameRequest.setSession(session);

        IGame testGame = new Game(2, "lobbyId");
        when(gameManagement.getGame("lobbyId")).thenReturn(testGame);
        ILobby lobby = new Lobby("lobbyId", "Test", List.of(user), user, 4);
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(lobby);

        post(userLeavedGameRequest);

        verify(gameManagement, times(1)).removePlayer("lobbyId", user);
        verify(lobbyManagement, times(1)).removeUser("lobbyId", user.getUsername());
        assertInstanceOf(BoardUpdateEvent.class, event);
    }

    @Test
    void testOnUserLeavedGameRequest_emptyLobby() throws LobbyIsEmptyException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));

        UserLeavedGameRequest userLeavedGameRequest = new UserLeavedGameRequest("lobbyId");
        userLeavedGameRequest.setSession(session);

        IGame testGame = new Game(2, "lobbyId");
        when(gameManagement.getGame("lobbyId")).thenReturn(testGame);
        ILobby lobby = new Lobby("lobbyId", "Test", List.of(user), user, 4);
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(lobby);

        doThrow(LobbyIsEmptyException.class).when(gameManagement)
                                            .removePlayer("lobbyId", user);

        post(userLeavedGameRequest);

        assertInstanceOf(LobbyClosedEvent.class, event);
    }

    @Test
    void testOnUserLeavedGameRequest_lobbyNotFound() throws LobbyNotFoundException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));

        UserLeavedGameRequest userLeavedGameRequest = new UserLeavedGameRequest("lobbyId");
        userLeavedGameRequest.setSession(session);

        IGame testGame = new Game(2, "lobbyId");
        when(gameManagement.getGame("lobbyId")).thenReturn(testGame);
        ILobby lobby = new Lobby("lobbyId", "Test", List.of(user), user, 4);
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(lobby);

        doThrow(LobbyNotFoundException.class).when(lobbyManagement)
                                             .removeUser("lobbyId", user.getUsername());

        post(userLeavedGameRequest);

        assertInstanceOf(StatusResponse.class, event);
    }

    @Test
    void testOnAvailableShareKnowledgePlayersRequestWithCityCard() throws SessionNotFoundException, InterruptedException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));

        AvailableShareKnowledgePlayersRequest request = new AvailableShareKnowledgePlayersRequest("lobbyId");
        request.setSession(session);

        IGame game = new Game(2, "lobbyId");
        IPlayer currentPlayer = new Player(user, "lobbyId");
        currentPlayer.setCurrentPosition(new CityRepository().getCity(1));
        currentPlayer.getCards()
                     .add(new CityCard(1, "Barcelona", mock(ICity.class)));
        currentPlayer.setRole(new CountryDoctor());
        game.getPlayers()
            .add(currentPlayer);
        game.setCurrentPlayerIndex(0);

        when(gameManagement.getGame("lobbyId")).thenReturn(game);
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(new Lobby("lobbyId", "Test", List.of(user), user, 4));

        postAndWait(request);

        verify(gameManagement, times(1)).lockGameInWaitForConfirmation("lobbyId");
    }

    @Test
    void testOnAvailableShareKnowledgePlayersRequestWithoutCityCard() throws SessionNotFoundException, InterruptedException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));

        AvailableShareKnowledgePlayersRequest request = new AvailableShareKnowledgePlayersRequest("lobbyId");
        request.setSession(session);

        IGame game = new Game(2, "lobbyId");
        IPlayer currentPlayer = new Player(user, "lobbyId");
        currentPlayer.setCurrentPosition(new CityRepository().getCity(1));
        currentPlayer.setRole(new CountryDoctor());

        game.getPlayers()
            .add(currentPlayer);
        game.setCurrentPlayerIndex(0);

        IPlayer playerWithCityCard = new Player(new User("otheruser", "otherpassword"), "lobbyId");
        playerWithCityCard.getCards()
                          .add(new CityCard(1, "Madrid", mock(ICity.class)));
        game.getPlayers()
            .add(playerWithCityCard);
        playerWithCityCard.setRole(new Sailor());

        when(gameManagement.getGame("lobbyId")).thenReturn(game);
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(new Lobby("lobbyId", "Test", List.of(user), user, 4));
        when(authenticationService.getSession(playerWithCityCard.getUser())).thenReturn(Optional.of(UUIDSession.create(
                playerWithCityCard.getUser())));

        postAndWait(request);

        verify(gameManagement, times(1)).lockGameInWaitForConfirmation("lobbyId");
    }


    @Test
    void testOnCardExchangeConfirmationRequest() {
        CardExchangeConfirmationRequest request = new CardExchangeConfirmationRequest(
                "lobbyId",
                true,
                mock(CardExchangeConfirmationEvent.class)
        );
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        request.setSession(session);

        IGame game = new Game(2, "lobbyId");
        when(gameManagement.getGame("lobbyId")).thenReturn(game);
        ILobby lobby = new Lobby("lobbyId", "Test", List.of(user), user, 4);
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(lobby);

        gameService.onCardExchangeConfirmationRequest(request);

        verify(gameManagement, times(1)).unlockGameInWaitForConfirmation("lobbyId");
        verify(gameManagement, times(1)).giveCard(request.getCardExchangeConfirmationEvent());
        verify(gameService, times(2)).sendToAllInLobby(eq(lobby), any(BoardUpdateEvent.class));
    }

    @Test
    void testOnPoliticianSecondRoleActionRequest() {
        PoliticianSecondRoleActionRequest request = new PoliticianSecondRoleActionRequest("lobbyId");
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        request.setSession(session);

        IGame game = new Game(2, "lobbyId");
        IPlayer currentPlayer = new Player(user, "lobbyId");
        game.setCurrentPlayerIndex(0);
        game.getPlayers()
            .add(currentPlayer);
        currentPlayer.setRole(new Politician());
        when(gameManagement.getGame("lobbyId")).thenReturn(game);
        when(gameManagement.getAvailableCardsForPoliticianSecondRoleAction("lobbyId")).thenReturn(Map.of());
        when(authenticationService.getSession(user)).thenReturn(Optional.of(session));
        when(lobbyManagement.getLobby(any())).thenReturn(mock(ILobby.class));
        gameService.onPoliticianSecondRoleActionRequest(request);

        verify(gameManagement, times(1)).lockGameInWaitForConfirmation("lobbyId");
        verify(gameService, times(1)).post(any(PoliticianSecondRoleActionResponse.class));
    }

    @Test
    void testOnCardExchangeWithDiscardPileRequest() {
        CardsExchangeWithDiscardPileRequest request = new CardsExchangeWithDiscardPileRequest(
                Map.of(
                        "cardKey",
                        mock(ICardDTO.class)
        ), "lobbyId"
        );
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        request.setSession(session);

        IGame game = new Game(2, "lobbyId");
        when(gameManagement.getGame("lobbyId")).thenReturn(game);
        ILobby lobby = new Lobby("lobbyId", "Test", List.of(user), user, 4);
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(lobby);
        game.setState(new PlayerTurnState());

        gameService.onCardExchangeWithDiscardPileRequest(request);

        verify(gameManagement, times(1)).unlockGameInWaitForConfirmation("lobbyId");
        verify(gameManagement, times(1)).swapCardsWithDiscardPile(request.getCardsToExchange(), "lobbyId");
        verify(gameService, times(2)).sendToAllInLobby(eq(lobby), any(BoardUpdateEvent.class));
    }

    @Test
    void testOnCardsExchangeRequest() throws SessionNotFoundException {
        String lobbyId = "lobby123";
        String requestingPlayer = "player1";
        String otherPlayer = "player2";
        ICardDTO requestingPlayerCard = mock(ICardDTO.class);
        ICardDTO otherPlayerCard = mock(ICardDTO.class);
        Map<String, ICardDTO> map = new HashMap<>();
        map.put(requestingPlayer, requestingPlayerCard);
        map.put(otherPlayer, otherPlayerCard);
        CardsExchangeRequest request = new CardsExchangeRequest(map, lobbyId, requestingPlayer);
        Session session = mock(Session.class);
        when(session.getUser()).thenReturn(mock(IUserDTO.class));
        when(authenticationService.getSession(any())).thenReturn(Optional.of(session));
        IGame game = mock(IGame.class);
        when(gameManagement.getGame(lobbyId)).thenReturn(game);
        ILobby lobby = mock(ILobby.class);
        when(lobbyManagement.getLobby(lobbyId)).thenReturn(lobby);
        when(game.getCityRepository()).thenReturn(mock(CityRepository.class));
        when(game.getConnectionRepository()).thenReturn(mock(ConnectionRepository.class));
        when(game.getRegionRepository()).thenReturn(mock(RegionRepository.class));
        when(game.getPlagueRepository()).thenReturn(mock(PlagueRepository.class));
        when(game.getState()).thenReturn(new PlayerTurnState());
        when(game.getPlayer(any())).thenReturn(mock(Player.class));
        game.setState(new PlayerTurnState());

        gameService.onCardsExchangeRequest(request);

        verify(gameManagement, times(1)).getGame(lobbyId);
    }

    @Test
    void testOnSwapCardsConfirmedRequest() {
        SwapCardsConfirmedRequest request = new SwapCardsConfirmedRequest(
                "lobbyId",
                true,
                mock(SwapCardsConfirmationEvent.class)

        );
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        request.setSession(session);

        IGame game = new Game(2, "lobbyId");
        when(gameManagement.getGame("lobbyId")).thenReturn(game);
        ILobby lobby = new Lobby("lobbyId", "Test", List.of(user), user, 4);
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(lobby);

        gameService.onSwapCardsConfirmedRequest(request);

        verify(gameManagement, times(1)).swapCards(request);
    }
}