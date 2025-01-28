package de.uol.swp.server.connection;

import de.uol.swp.common.connection.request.AvailableDestinationsRequest;
import de.uol.swp.common.connection.response.AvailableDestinationsResponse;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.connection.management.ConnectionManagement;
import de.uol.swp.server.connection.management.IConnectionManagement;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// disabled sonar lint rule java:S5786, because public class modifier is required for EventBusBasedTest
public class ConnectionServiceTest extends EventBusBasedTest {
    @Mock
    IConnectionManagement connectionManagement;

    ConnectionService connectionService;

    CityRepository cityRepository;

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
}
