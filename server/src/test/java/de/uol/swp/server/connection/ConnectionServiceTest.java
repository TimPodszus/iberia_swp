package de.uol.swp.server.connection;

import de.uol.swp.common.connection.request.AvailableDestinationsRequest;
import de.uol.swp.common.connection.response.AvailableDestinationsResponse;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.connection.management.ConnectionManagement;
import de.uol.swp.server.connection.management.IConnectionManagement;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// disabled sonar lint rule java:S5786, because public class modifier is required for EventBusBasedTest
public class ConnectionServiceTest extends EventBusBasedTest {
    @Mock
    IConnectionManagement connectionManagement;

    ConnectionService connectionService;

    @Subscribe
    public void onEvent(AvailableDestinationsResponse e) {
        handleEvent(e);
    }

    /**
     * Sets up the test environment before each test.
     * Mocks the IConnectionManagement and initializes the ConnectionService.
     */
    @BeforeEach
    void setUp() {
        connectionManagement = mock(ConnectionManagement.class);
        connectionService = new ConnectionService(super.getBus(), connectionManagement);
    }

    /**
     * Tests the handling of an AvailableDestinationsRequest.
     *
     * @throws InterruptedException if the thread is interrupted while waiting for the event
     */
    @Test
    void testOnAvailableDestinationsRequest() throws InterruptedException {
        AvailableDestinationsRequest request = new AvailableDestinationsRequest("", "");
        when(connectionManagement.getAvailableDestinations("", "")).thenReturn(new ArrayList<>());

        postAndWait(request);

        assertInstanceOf(AvailableDestinationsResponse.class, event, "Expected an AvailableDestinationsResponse");
    }
}
