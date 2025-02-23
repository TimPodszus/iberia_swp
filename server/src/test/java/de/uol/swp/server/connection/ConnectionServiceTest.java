package de.uol.swp.server.connection;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.connection.request.AvailableDestinationsRequest;
import de.uol.swp.common.connection.request.BuildableTrainTracksRequest;
import de.uol.swp.common.connection.response.AvailableDestinationsResponse;
import de.uol.swp.common.game.TransportMode;
import de.uol.swp.common.connection.dto.DestinationInfo;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.connection.response.BuildableTrainTracksResponse;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.cards.events.MovePlayerAnywhereEvent;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.connection.data.Connection;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.connection.management.IConnectionManagement;
import de.uol.swp.server.game.GameException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
        when(connectionManagement.getAvailableDestinations("", 1)).thenReturn(Map.of(1,
                new DestinationInfo(List.of(), new ArrayList<>(List.of(TransportMode.NONE)))
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

    @Test
    void testOnMovePlayerAnywhereEvent() throws InterruptedException {
        IUser user = new User("testuser", "1234");
        Session session = UUIDSession.create(user);
        when(userManagement.getUser("testuser")).thenReturn(user);
        when(authenticationService.getSession(user)).thenReturn(java.util.Optional.of(session));
        when(connectionManagement.getAllDestinations("")).thenReturn(Map.of(1, new DestinationInfo(
                List.of(),
                new ArrayList<>(List.of(TransportMode.NONE))
        )));
        MovePlayerAnywhereEvent movePlayerAnywhereEvent = new MovePlayerAnywhereEvent("", "testuser");

        postAndWait(movePlayerAnywhereEvent);

        assertInstanceOf(AvailableDestinationsResponse.class, event, "Expected an AvailableDestinationsResponse");
    }

    /**
     * Tests the handling of a MovePlayerAnywhereEvent when the user is not logged in.
     * <p>
     * This test verifies that a GameException is thrown when a MovePlayerAnywhereEvent
     * is received for a user who is not logged in.
     */
    @Test
    void testOnMovePlayerAnywhereEventWithUserNotLoggedIn() {
        IUser user = new User("testuser", "1234");
        when(userManagement.getUser("testuser")).thenReturn(user);
        when(authenticationService.getSession(user)).thenReturn(java.util.Optional.empty());
        MovePlayerAnywhereEvent movePlayerAnywhereEvent = new MovePlayerAnywhereEvent("", "testuser");

        assertThrows(GameException.class,
                () -> connectionService.onMovePlayerAnywhereEvent(movePlayerAnywhereEvent),
                "Expected a GameException"
        );
    }

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
        assertEquals(
                true,
                ((BuildableTrainTracksResponse) event).getConnections().get(0).isTrainTrackBuildable(),
                "Expected a train track"
        );
    }
}
