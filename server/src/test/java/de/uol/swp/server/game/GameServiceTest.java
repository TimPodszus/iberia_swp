package de.uol.swp.server.game;

import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.common.game.message.response.CreateGameResponse;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.game.message.request.AvailableActionsRequest;
import de.uol.swp.common.game.message.response.AvailableActionsResponse;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagement;
import de.uol.swp.server.game.management.GameManagementException;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

/**
 * Test class for GameService.
 */
public class GameServiceTest extends EventBusBasedTest {

    private static final String LOBBY_CODE = "lobby123";
    private static final int GAME_ID = 1;

    @Mock
    ILobbyManagement lobbyManagement;

    @Mock
    IGameManagement gameManagement;

    @InjectMocks
    GameService gameService = new GameService(super.getBus(), lobbyManagement, gameManagement);

    private IGame game;
    private ILobby lobby;

    private CreateGameRequest createGameRequest;
    private PositioningRequest positioningRequest;

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

    @BeforeEach
    void setUp() throws LobbyManagementException, PlayerManagementException, GameManagementException {
        MockitoAnnotations.openMocks(this);
        gameManagement = mock(GameManagement.class);
        lobbyManagement = mock(ILobbyManagement.class);

        gameService = new GameService(getBus(), lobbyManagement, gameManagement);

        List<IUserDTO> users = new ArrayList<>();
        users.add(new UserDTO("test", "test"));

        createGameRequest = new CreateGameRequest(LOBBY_CODE, GAME_ID, users);
        positioningRequest = mock(PositioningRequest.class);

        game = mock(IGame.class);
        lobby = mock(ILobby.class);

        when(gameManagement.createAndInitializeGame(createGameRequest)).thenReturn(game);
        when(gameManagement.setPositioning(positioningRequest)).thenReturn(game);
        when(lobbyManagement.getLobby(LOBBY_CODE)).thenReturn(Optional.of(lobby));

    }

    /**
     * Tests setting player positioning when the game is null.
     */
    /**
     * Tests the handling of AvailableActionsRequest.
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
    void testOnPositionRequest_LobbyNotFound() throws LobbyManagementException, GameManagementException {
        when(lobbyManagement.getLobby(LOBBY_CODE)).thenReturn(Optional.empty());
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
    void testOnPositionRequest_GameIsNull() throws LobbyManagementException, GameManagementException {
        when(gameManagement.setPositioning(positioningRequest)).thenReturn(null);
        when(positioningRequest.getLobbyId()).thenReturn(LOBBY_CODE);

        post(positioningRequest);

        verify(gameManagement, times(1)).setPositioning(positioningRequest);
        assertNull(event, "No event should be posted when the game is null.");
    }

    /**
     * Utility to set private fields via reflection.
     */
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set private field: " + fieldName, e);
        }
    }

}
