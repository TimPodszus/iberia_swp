package de.uol.swp.server.Game;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.event.ShareKnowledgeEvent;
import de.uol.swp.common.game.message.request.*;
import de.uol.swp.common.game.message.event.EndGameEvent;
import de.uol.swp.common.game.message.request.AvailableActionsRequest;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.common.game.message.request.ShareRideRequest;
import de.uol.swp.common.game.message.response.AvailableActionsResponse;
import de.uol.swp.common.game.message.response.CreateGameResponse;
import de.uol.swp.common.player.request.MovePlayerRequest;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.game.GameException;
import de.uol.swp.server.game.GameService;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagementException;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.states.DrawCardState;
import de.uol.swp.server.game.states.EndGameState;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.greenrobot.eventbus.EventBusException;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @InjectMocks
    GameService gameService = new GameService(
            getBus(),
            lobbyManagement,
            gameManagement,
            cityManagement,
            playerManagement
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
     * @throws InterruptedException    if the thread is interrupted
     * @throws GameManagementException if there is an error in game management
     */
    @Test
    void testOnMovePlayerRequest() throws InterruptedException, GameManagementException {
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
        IGame game = new Game(2, "lobbycode");
        when(gameManagement.getGame("lobbycode")).thenReturn(game);
        ILobby lobby = new Lobby("lobbycode", "Test", List.of(user, user2), user, 4);
        when(lobbyManagement.getLobby("lobbycode")).thenReturn(lobby);

        postAndWait(movePlayerRequest);

        verify(gameManagement, atLeast(1)).movePlayer(user, "lobbycode", city, null);
        verify(gameManagement, atLeast(1)).lockGameInWaitForConfirmation("lobbycode");
        assertInstanceOf(BoardUpdateEvent.class, event);
    }

    /**
     * Tests the onMovePlayerRequest method with an unknown player to take with.
     *
     * @throws GameManagementException if there is an error in game management
     */
    @Test
    void testOnMovePlayerRequestWithUnknownPlayerToTakeWith() throws GameManagementException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));

        IUser user2 = new User("testuser2", "testpassword2");

        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("lobbycode", 12, "testuser2");
        movePlayerRequest.setSession(session);

        ICity city = new CityRepository().getCity(12);
        when(cityManagement.getCity("lobbycode", 12)).thenReturn(city);
        IGame game = new Game(2, "lobbycode");
        when(gameManagement.getGame("lobbycode")).thenReturn(game);
        ILobby lobby = new Lobby("lobbycode", "Test", List.of(user, user2), user, 4);
        when(lobbyManagement.getLobby("lobbycode")).thenReturn(lobby);

        assertThrows(EventBusException.class, () -> postAndWait(movePlayerRequest));

        verify(gameManagement, atLeast(1)).movePlayer(user, "lobbycode", city, null);
    }

    /**
     * Tests the onMovePlayerRequest method with a not logged-in player to take with.
     *
     * @throws GameManagementException if there is an error in game management
     */
    @Test
    void testOnMovePlayerRequestWithNotLoggedInPlayerToTakeWith() throws GameManagementException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));

        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("lobbycode", 12, "testuser2");
        movePlayerRequest.setSession(session);

        ICity city = new CityRepository().getCity(12);
        when(cityManagement.getCity("lobbycode", 12)).thenReturn(city);
        IGame game = new Game(2, "lobbycode");
        when(gameManagement.getGame("lobbycode")).thenReturn(game);
        ILobby lobby = new Lobby("lobbycode", "Test", List.of(user), user, 4);
        when(lobbyManagement.getLobby("lobbycode")).thenReturn(lobby);

        assertThrows(EventBusException.class, () -> postAndWait(movePlayerRequest));

        verify(gameManagement, atLeast(1)).movePlayer(user, "lobbycode", city, null);
    }

    /**
     * Tests the onMovePlayerRequest method with an unknown user.
     */
    @Test
    void testOnMovePlayerRequestWithUnknownUser() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("lobbycode", 12, 1);
        movePlayerRequest.setSession(null);

        assertThrows(GameException.class, () -> gameService.onMovePlayerRequest(movePlayerRequest));
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
     * Tests setting player positioning when the lobby is not found.
     *
     * @throws GameManagementException if there is an error in game management
     */
    @Test
    void testOnPositionRequest_LobbyNotFound() throws GameManagementException {
        PositioningRequest positioningRequest = mock(PositioningRequest.class);
        when(gameManagement.setPositioning(positioningRequest)).thenReturn(mock(IGame.class));
        when(lobbyManagement.getLobby(LOBBY_CODE)).thenReturn(null);
        when(positioningRequest.getLobbyId()).thenReturn(LOBBY_CODE);

        post(positioningRequest);

        verify(gameManagement, times(1)).setPositioning(positioningRequest);
        verify(lobbyManagement, times(1)).getLobby(LOBBY_CODE);
        assertNull(event, "No event should be posted when the lobby is not found.");
    }

    /**
     * Tests setting player positioning when the game is null.
     *
     * @throws GameManagementException if there is an error in game management
     */
    @Test
    void testOnPositionRequest_GameIsNull() throws GameManagementException {
        PositioningRequest positioningRequest = mock(PositioningRequest.class);
        when(gameManagement.setPositioning(positioningRequest)).thenReturn(mock(IGame.class));
        when(gameManagement.setPositioning(positioningRequest)).thenReturn(null);
        when(positioningRequest.getLobbyId()).thenReturn(LOBBY_CODE);

        post(positioningRequest);

        verify(gameManagement, times(1)).setPositioning(positioningRequest);
        assertNull(event, "No event should be posted when the game is null.");
    }

    /**
     * Tests create game when the game is null.
     *
     * @throws PlayerManagementException if there is an error in player management
     */
    @Test
    void testOnCreateGameRequest_GameIsNull() throws PlayerManagementException {
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
     * Tests the onShareKnowledgeRequest method when the request is accepted.
     *
     * @throws GameManagementException   if there is an error in game management
     * @throws PlayerManagementException if there is an error in player management
     */
    @Test
    void testOnShareKnowledgeRequest_Accepted() throws GameManagementException, PlayerManagementException {
        ShareKnowledgeEvent event = new ShareKnowledgeEvent(
                "lobbyId",
                "currentPlayer",
                "targetPlayer",
                mock(ICardDTO.class),
                mock(ICardDTO.class)
        );
        ShareKnowledgeRequest request = new ShareKnowledgeRequest("lobbyId", true, event);

        IGame game = mock(IGame.class);
        IPlayer currentPlayer = mock(IPlayer.class);
        IPlayer targetPlayer = mock(IPlayer.class);
        IUser currentUser = mock(IUser.class);
        IUser targetUser = mock(IUser.class);
    @Test
    void testOnGameStateChange_DrawCardState() {
        ILobby lobby = mock(ILobby.class);
        IGame game = mock(IGame.class);
        when(lobbyManagement.getLobby("gameId")).thenReturn(lobby);
        DrawCardState drawCardState = new DrawCardState();
        when(game.getState()).thenReturn(drawCardState);
        when(game.getPlayerCardDrawPile()).thenReturn(List.of());
        when(game.getGameId()).thenReturn("gameId");

        gameService.onGameStateChange(game);

        verify(game).setState(any(EndGameState.class));
    }

        when(gameManagement.getGame("lobbyId")).thenReturn(game);
        when(game.getCurrentPlayer()).thenReturn(currentPlayer);
        when(game.getPlayers()).thenReturn(List.of(currentPlayer, targetPlayer));
        when(currentPlayer.getUser()).thenReturn(currentUser);
        when(targetPlayer.getUser()).thenReturn(targetUser);
        when(currentUser.getUsername()).thenReturn("currentPlayer");
        when(targetUser.getUsername()).thenReturn("targetPlayer");

        gameService.onShareKnowledgeRequest(request);

        verify(gameManagement).unlockGameInWaitForConfirmation("lobbyId");
        verify(gameManagement).shareKnowledgeRequestAccepted(
                eq(currentPlayer),
                eq(targetPlayer),
                eq("lobbyId"),
                eq(event),
                eq(lobbyManagement),
                eq(gameService)
        );
    }

    /**
     * Tests the onShareKnowledgeRequest method when the request is denied.
     *
     * @throws GameManagementException   if there is an error in game management
     * @throws PlayerManagementException if there is an error in player management
     */
    @Test
    void testOnShareKnowledgeRequest_Denied() throws GameManagementException, PlayerManagementException {
        ShareKnowledgeEvent event = new ShareKnowledgeEvent(
                "lobbyId",
                "currentPlayer",
                "targetPlayer",
                mock(ICardDTO.class),
                mock(ICardDTO.class)
        );
        ShareKnowledgeRequest request = new ShareKnowledgeRequest("lobbyId", false, event);

        IGame game = mock(IGame.class);
        IPlayer currentPlayer = mock(IPlayer.class);
        IPlayer targetPlayer = mock(IPlayer.class);
        IUser currentUser = mock(IUser.class);
        IUser targetUser = mock(IUser.class);

        when(gameManagement.getGame("lobbyId")).thenReturn(game);
        when(game.getCurrentPlayer()).thenReturn(currentPlayer);
        when(game.getPlayers()).thenReturn(List.of(currentPlayer, targetPlayer));
        when(currentPlayer.getUser()).thenReturn(currentUser);
        when(targetPlayer.getUser()).thenReturn(targetUser);
        when(currentUser.getUsername()).thenReturn("currentPlayer");
        when(targetUser.getUsername()).thenReturn("targetPlayer");

        gameService.onShareKnowledgeRequest(request);

        verify(gameManagement).unlockGameInWaitForConfirmation("lobbyId");
        verify(gameManagement).postShareKnowledgeResponse(eq(event), eq(lobbyManagement), eq(gameService), eq(false));
    }
}