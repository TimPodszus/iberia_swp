package de.uol.swp.server.city;

import de.uol.swp.common.city.message.request.BuildHospitalRequest;
import de.uol.swp.common.city.message.request.HospitalFoundationEventRequest;
import de.uol.swp.common.city.message.response.HospitalFoundationEventResponse;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.eventcards.EventCard;
import de.uol.swp.server.cards.events.HospitalFoundationEvent;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.states.EventState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;
import de.uol.swp.server.usermanagement.management.ServerUserService;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CityServiceTest extends EventBusBasedTest {
    private static final String LOBBY_CODE = "testLobbyId";

    @Mock
    private ICityManagement cityManagement;

    @Mock
    private IGameManagement gameManagement;

    @Mock
    private ILobbyManagement lobbyManagement;

    @Mock
    private ServerUserService userManagement;

    @Mock
    private AuthenticationService authenticationService;

    @Spy
    @InjectMocks
    CityService cityService = new CityService(
            getBus(),
            cityManagement,
            gameManagement,
            lobbyManagement,
            userManagement
    );

    @Subscribe
    public void onBoardUpdateEvent(BoardUpdateEvent event) {
        super.handleEvent(event);
    }

    @Subscribe
    public void onHospitalFoundationEvent(HospitalFoundationEventResponse response) {
        super.handleEvent(response);
    }

    /**
     * Sets up the test environment.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOnBuildHospitalRequest_Successful() throws GameException, InterruptedException {
        IUser user = new User("testUser", "testPassword");
        Session session = UUIDSession.create(user);

        BuildHospitalRequest request = new BuildHospitalRequest(LOBBY_CODE, 1);
        request.setSession(session);

        doNothing().when(cityManagement)
                   .buildHospital(any(String.class), any(String.class), any(Integer.class));

        IGame game = new Game(2, "lobbycode");
        when(gameManagement.getGame(any(String.class))).thenReturn(game);

        ILobby lobby = new Lobby("lobbycode", "Test", List.of(user), user, 4);
        when(lobbyManagement.getLobby(any(String.class))).thenReturn(lobby);

        doNothing().when(cityService)
                   .sendToAllInLobby(any(Lobby.class), any(BoardUpdateEvent.class));

        postAndWait(request);

        verify(cityManagement, atLeast(1)).buildHospital(any(String.class), any(String.class), any(Integer.class));
    }

    @Test
    void testOnBuildHospitalRequest_Error() throws GameException {
        IUser user = new User("testUser", "testPassword");
        Session session = UUIDSession.create(user);

        BuildHospitalRequest request = new BuildHospitalRequest(LOBBY_CODE, 1);
        request.setSession(session);

        doThrow(new GameException("Test")).when(cityManagement)
                                          .buildHospital(any(String.class), any(String.class), any(Integer.class));

        IGame game = new Game(2, "lobbycode");
        when(gameManagement.getGame(any(String.class))).thenReturn(game);

        ILobby lobby = new Lobby("lobbycode", "Test", List.of(user), user, 4);
        when(lobbyManagement.getLobby(any(String.class))).thenReturn(lobby);

        doNothing().when(cityService)
                   .sendToAllInLobby(any(Lobby.class), any(BoardUpdateEvent.class));

        cityService.onBuildHospitalRequest(request);

        verify(cityManagement, atLeast(1)).buildHospital(any(String.class), any(String.class), any(Integer.class));
    }

    @Test
    void testBuildHospitalRequestEquals() {
        BuildHospitalRequest request1 = new BuildHospitalRequest(LOBBY_CODE, 1);
        BuildHospitalRequest request2 = new BuildHospitalRequest(LOBBY_CODE, 1);
        BuildHospitalRequest request3 = new BuildHospitalRequest(LOBBY_CODE, 2);
        BuildHospitalRequest request4 = new BuildHospitalRequest("differentLobbyId", 1);

        // Test for the same object
        assertEquals(request1, request1);

        // Test for null
        // Warning: Arguments to 'assertNotEquals()' in wrong order -> needed for full coverage
        assertNotEquals(request1, null);

        // Test for different class
        // Warning: Arguments to 'assertNotEquals()' in wrong order -> needed for full coverage
        assertNotEquals(request1, new Object());

        // Test for equal objects
        assertEquals(request1, request2);

        // Test for different cityId
        assertNotEquals(request1, request3);

        // Test for different lobbyId
        assertNotEquals(request1, request4);
    }

    @Test
    void testBuildHospitalRequestHashCode() {
        BuildHospitalRequest request1 = new BuildHospitalRequest(LOBBY_CODE, 1);
        BuildHospitalRequest request2 = new BuildHospitalRequest(LOBBY_CODE, 1);
        BuildHospitalRequest request3 = new BuildHospitalRequest(LOBBY_CODE, 2);

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testOnHospitalFoundationEvent_Successful() throws InterruptedException {
        IUser user = new User("testUser", "testPassword");
        Session session = UUIDSession.create(user);
        IGame game = new Game(2, LOBBY_CODE);

        HospitalFoundationEvent event = new HospitalFoundationEvent(LOBBY_CODE, user.getUsername());

        IPlayer player = new Player(user);
        game.getPlayers()
            .add(player);
        game.getPlayer(user.getUsername())
            .setCurrentPosition(game.getCityRepository()
                                    .getCity(1));
        when(gameManagement.getGame(LOBBY_CODE)).thenReturn(game);
        when(userManagement.getUser(user.getUsername())).thenReturn(user);
        when(authenticationService.getSession(user)).thenReturn(Optional.of(session));

        postAndWait(event);

        assertInstanceOf(HospitalFoundationEventResponse.class, this.event);
    }

    @Test
    void testOnHospitalFoundationEvent_SessionNotFound() {
        IUser user = new User("testUser", "testPassword");
        IGame game = new Game(2, LOBBY_CODE);

        HospitalFoundationEvent event = new HospitalFoundationEvent(LOBBY_CODE, user.getUsername());

        IPlayer player = new Player(user);
        game.getPlayers()
            .add(player);
        game.getPlayer(user.getUsername())
            .setCurrentPosition(game.getCityRepository()
                                    .getCity(1));
        when(gameManagement.getGame(LOBBY_CODE)).thenReturn(game);
        when(userManagement.getUser(user.getUsername())).thenReturn(user);

        assertThrows(SessionNotFoundException.class, () -> cityService.onHospitalFoundationEvent(event));
    }

    @Test
    void testOnHospitalFoundationRequest_Successful() throws InterruptedException {
        IUser user = new User("testUser", "testPassword");
        IGame game = new Game(2, LOBBY_CODE);
        game.setState(new PlayerTurnState());
        game.setState(new EventState(mock(EventCard.class)));
        when(gameManagement.getGame(LOBBY_CODE)).thenReturn(game);
        doNothing().when(cityManagement)
                   .buildHospitalWithEventCard(LOBBY_CODE, 1);
        when(lobbyManagement.getLobby(LOBBY_CODE)).thenReturn(new Lobby(LOBBY_CODE, "Test", List.of(user), user, 2));

        postAndWait(new HospitalFoundationEventRequest(LOBBY_CODE, 1));

        assertInstanceOf(PlayerTurnState.class, game.getState());
        assertInstanceOf(BoardUpdateEvent.class, this.event);
    }
}
