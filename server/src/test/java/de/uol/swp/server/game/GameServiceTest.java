package de.uol.swp.server.game;

import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.player.request.MovePlayerRequest;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.common.game.message.request.AvailableActionsRequest;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.common.game.message.response.AvailableActionsResponse;
import de.uol.swp.common.game.message.response.CreateGameResponse;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagementException;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @InjectMocks
    GameService gameService = new GameService(getBus(), lobbyManagement, gameManagement, cityManagement);

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

    @Subscribe
    public void onEvent(AvailableActionsResponse event) {
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
     * @throws InterruptedException     if the thread is interrupted
     * @throws GameManagementException  if there is an error in game management
     * @throws LobbyManagementException if there is an error in lobby management
     */
    @Test
    void testOnMovePlayerRequest() throws InterruptedException, GameManagementException, LobbyManagementException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("lobbycode", 12);
        movePlayerRequest.setSession(session);
        ICity city = new CityRepository().getCity(12);
        when(cityManagement.getCity("lobbycode", 12)).thenReturn(city);
        IGame game = new Game(2, "lobbycode");
        when(gameManagement.getGame("lobbycode")).thenReturn(game);
        ILobby lobby = new Lobby("lobbycode", "Test", List.of(user), user, 4);
        when(lobbyManagement.getLobby("lobbycode")).thenReturn(lobby);

        postAndWait(movePlayerRequest);

        verify(gameManagement, atLeast(1)).movePlayer(user, "lobbycode", city);
        assertInstanceOf(BoardUpdateEvent.class, event);
    }

    /**
     * Tests the onMovePlayerRequest method with an unknown user.
     */
    @Test
    void testOnMovePlayerRequestWithUnknownUser() {
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("lobbycode", 12);
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
     */
    @Test
    void testOnPositionRequest_LobbyNotFound() throws LobbyManagementException, GameManagementException, PlayerManagementException {
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
     */
    @Test
    void testOnPositionRequest_GameIsNull() throws GameManagementException, PlayerManagementException {
        PositioningRequest positioningRequest = mock(PositioningRequest.class);
        when(gameManagement.setPositioning(positioningRequest)).thenReturn(mock(IGame.class));
        when(gameManagement.setPositioning(positioningRequest)).thenReturn(null);
        when(positioningRequest.getLobbyId()).thenReturn(LOBBY_CODE);

        post(positioningRequest);

        verify(gameManagement, times(1)).setPositioning(positioningRequest);
        assertNull(event, "No event should be posted when the game is null.");
    }

    /**
     * Tests create game  when the game is null.
     */
    @Test
    void testOnCreateGameRequest_GameIsNull() throws LobbyManagementException, PlayerManagementException {
        List<IUserDTO> users = List.of(new UserDTO("username", "password"));
        CreateGameRequest createGameRequest = new CreateGameRequest(LOBBY_CODE, GAME_ID, users);
        when(gameManagement.createAndInitializeGame(createGameRequest)).thenReturn(null);
        when(lobbyManagement.getLobby(LOBBY_CODE)).thenReturn(mock(Lobby.class));

        gameService.onCreateGameRequest(createGameRequest);

        verify(gameManagement, times(1)).createAndInitializeGame(createGameRequest);
        verify(lobbyManagement, times(1)).getLobby(LOBBY_CODE);
        assertNull(event, "No event should be posted when the game creation fails.");
    }


}
