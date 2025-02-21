package de.uol.swp.server.connection;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.connection.request.AvailableDestinationsRequest;
import de.uol.swp.common.connection.request.BuildableTrainTracksRequest;
import de.uol.swp.common.connection.response.AvailableDestinationsResponse;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.connection.response.BuildableTrainTracksResponse;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.cards.events.MovePlayerAnywhereEvent;
import de.uol.swp.server.cards.events.StateMobilizationEvent;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.connection.data.Connection;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.connection.management.IConnectionManagement;
import de.uol.swp.server.game.GameException;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.usermanagement.management.ServerUserService;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// disabled sonar lint rule java:S5786, because public class modifier is required for EventBusBasedTest
public class ConnectionServiceTest extends EventBusBasedTest {
    @Mock
    IConnectionManagement connectionManagement;

    @Mock
    ServerUserService userManagement;

    @Mock
    AuthenticationService authenticationService;

    @InjectMocks
    ConnectionService connectionService = new ConnectionService(super.getBus(), connectionManagement, userManagement);

    CityRepository cityRepository;

    @Subscribe
    public void onEvent(AvailableDestinationsResponse e) {
        handleEvent(e);
    }

    @Subscribe
    public void onEvent(BuildableTrainTracksResponse e) {
        handleEvent(e);
    }

    /**
     * Sets up the test environment before each test.
     * Mocks the IConnectionManagement and initializes the ConnectionService.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cityRepository = new CityRepository();
    }

    /**
     * Tests the handling of an AvailableDestinationsRequest.
     *
     * @throws InterruptedException if the thread is interrupted while waiting for the event
     */
    @Test
    void testOnAvailableDestinationsRequest() throws InterruptedException {
        AvailableDestinationsRequest request = new AvailableDestinationsRequest("", 1);
        when(connectionManagement.getAvailableDestinations("", 1)).thenReturn(Map.of(cityRepository.getCity(1),
                List.of()
        ));

        postAndWait(request);

        assertInstanceOf(AvailableDestinationsResponse.class, event, "Expected an AvailableDestinationsResponse");
        assertEquals(1,
                ((AvailableDestinationsResponse) event).getCities()
                                                       .size(),
                "Expected 1 available destination"
        );
        assertEquals(1,
                ((AvailableDestinationsResponse) event).getCities()
                                                       .keySet()
                                                       .iterator()
                                                       .next(),
                "Expected the city to be the available destination"
        );
    }

    /**
     * Tests the handling of a MovePlayerAnywhereEvent.
     * <p>
     * This test verifies that an AvailableDestinationsResponse is received
     * when a MovePlayerAnywhereEvent is posted.
     *
     * @throws InterruptedException if the thread is interrupted while waiting for the event
     */
    @Test
    void testOnMovePlayerAnywhereEvent() throws InterruptedException {
        createUserAndSession("testuser");
        when(connectionManagement.getAllDestinations("")).thenReturn(Map.of(cityRepository.getCity(1), List.of()));
        MovePlayerAnywhereEvent movePlayerAnywhereEvent = new MovePlayerAnywhereEvent("", "testuser");

        postAndWait(movePlayerAnywhereEvent);

        assertInstanceOf(AvailableDestinationsResponse.class, event, "Expected an AvailableDestinationsResponse");
    }

    @Test
    void testOnMovePlayerAnywhereEventWithUserNotLoggedIn() {
        IUser user = createUserAndSession("testuser");
        when(authenticationService.getSession(user)).thenReturn(java.util.Optional.empty());
        MovePlayerAnywhereEvent movePlayerAnywhereEvent = new MovePlayerAnywhereEvent("", "testuser");

        assertThrows(GameException.class,
                () -> connectionService.onMovePlayerAnywhereEvent(movePlayerAnywhereEvent),
                "Expected a GameException"
        );
    }

    /**
     * Tests the handling of a BuildableTrainTracksRequest.
     * <p>
     * This test verifies that a BuildableTrainTracksResponse is received
     * when a BuildableTrainTracksRequest is posted.
     *
     * @throws InterruptedException if the thread is interrupted while waiting for the event
     */
    @Test
    void testOnBuildableTrainTracksRequest() throws InterruptedException {
        BuildableTrainTracksRequest request = new BuildableTrainTracksRequest("", 1);
        List<IConnection> demoList = List.of(
                new Connection(1, List.of(CityName.ALBACETE, CityName.ALICANTE), true)
        );
        when(connectionManagement.getBuildableTrainTracks("", 1)).thenReturn(demoList);

        postAndWait(request);

        assertInstanceOf(BuildableTrainTracksResponse.class, event, "Expected a BuildableTrainTracksResponse");
        assertEquals(
                1,
                ((BuildableTrainTracksResponse) event).getConnections().get(0).getId(),
                "Expected connection ID 1"
        );
        assertEquals(
                List.of(CityName.ALBACETE, CityName.ALICANTE),
                ((BuildableTrainTracksResponse) event).getConnections().get(0).getCityNames(),
                "Expected connection between Albacete and Alicante"
        );
        assertTrue(((BuildableTrainTracksResponse) event).getConnections()
                                                         .get(0)
                                                         .isTrainTrackBuildable(), "Expected a train track");
    }

    /**
     * Tests the handling of a StateMobilizationEvent.
     * <p>
     * This test verifies that an AvailableDestinationsResponse is received
     * when a StateMobilizationEvent is posted.
     *
     * @throws InterruptedException if the thread is interrupted while waiting for the event
     */
    @Test
    void testOnStateMobilizationEvent() throws InterruptedException {
        IUser testuser1 = createUserAndSession("testuser1");
        IPlayer player1 = createMockPlayer(testuser1, 1);

        IUser testuser2 = createUserAndSession("testuser2");
        IPlayer player2 = createMockPlayer(testuser2, 1);

        IGame game = createMockGame(List.of(player1, player2));
        when(connectionManagement.getGame("1234")).thenReturn(game);
        when(connectionManagement.getAvailableDestinations("", 1)).thenReturn(Map.of(cityRepository.getCity(1),
                List.of()
        ));

        StateMobilizationEvent stateMobilizationEvent = new StateMobilizationEvent("1234");

        postAndWait(stateMobilizationEvent);

        assertInstanceOf(AvailableDestinationsResponse.class, event, "Expected an AvailableDestinationsResponse");
        verify(connectionManagement, times(2)).getAvailableDestinations("1234", 1);
    }

    /**
     * Creates a user and session for the given username.
     *
     * @param username the username of the user
     * @return the created IUser instance
     */
    private IUser createUserAndSession(String username) {
        IUser user = new User(username, "1234");
        Session session = UUIDSession.create(user);
        when(userManagement.getUser(username)).thenReturn(user);
        when(authenticationService.getSession(user)).thenReturn(java.util.Optional.of(session));
        return user;
    }

    /**
     * Creates a mock player for the given user and city ID.
     *
     * @param user   the user associated with the player
     * @param cityId the ID of the city where the player is located
     * @return the created IPlayer instance
     */
    private IPlayer createMockPlayer(IUser user, int cityId) {
        IPlayer player = mock(Player.class);
        when(player.getUser()).thenReturn(user);
        when(player.getCurrentPosition()).thenReturn(cityRepository.getCity(cityId));
        return player;
    }

    /**
     * Creates a mock game with the given list of players.
     *
     * @param players the list of players in the game
     * @return the created IGame instance
     */
    private IGame createMockGame(List<IPlayer> players) {
        IGame game = mock(Game.class);
        when(game.getPlayers()).thenReturn(players);
        return game;
    }
}
