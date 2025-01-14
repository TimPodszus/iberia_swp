package de.uol.swp.server.game;

import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.player.request.MovePlayerRequest;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagementException;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test class for GameService.
 */
public class GameServiceTest extends EventBusBasedTest {

    @Mock
    private IGameManagement gameManagement;

    @Mock
    private ICityManagement cityManagement;

    @Mock
    private ILobbyManagement lobbyManagement;

    @InjectMocks
    GameService gameService = new GameService(getBus(), lobbyManagement);

    @Mock
    private AuthenticationService authenticationService;

    /**
     * Handles BoardUpdateEvent.
     *
     * @param e the BoardUpdateEvent
     */
    @Subscribe
    public void onEvent(BoardUpdateEvent e) {
        handleEvent(e);
    }

    /**
     * Sets up the test environment.
     *
     * @throws NoSuchFieldException   if the field is not found
     * @throws IllegalAccessException if the field is not accessible
     */
    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        Field authServiceField = AbstractService.class.getDeclaredField("authenticationService");
        authServiceField.setAccessible(true);
        authServiceField.set(gameService, authenticationService);
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
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("lobbycode", "12");
        movePlayerRequest.setSession(session);
        City city = new CityRepository().getCity("12");
        when(cityManagement.getCity("lobbycode", "12")).thenReturn(city);
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
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("lobbycode", "12");
        movePlayerRequest.setSession(null);

        assertThrows(GameException.class, () -> gameService.onMovePlayerRequest(movePlayerRequest));
    }
}
