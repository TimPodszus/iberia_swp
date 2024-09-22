package de.uol.swp.server.connection;

import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.connection.request.AvailableDestinationsRequest;
import de.uol.swp.common.connection.response.AvailableDestinationsResponse;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityName;
import de.uol.swp.server.plague.PlagueName;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

// disabled sonar lint rule java:S5786, because public class modifier is required for EventBusBasedTest
public class ConnectionServiceTest extends EventBusBasedTest {

    final ConnectionService connectionService = new ConnectionService(getBus(), new ConnectionManagement());

    @Subscribe
    public void onEvent(AvailableDestinationsResponse e) {
        handleEvent(e);
    }

    /**
     * Tests the handling of an AvailableDestinationsRequest.
     *
     * @throws InterruptedException if the thread is interrupted while waiting for the event
     */
    @Test
    void testOnAvailableDestinationsRequest() throws InterruptedException {
        ICityDTO city = new City(PlagueName.YELLOW_FEVER, CityName.PALMA_DE_MALLORCA, -123, true).toDto();
        AvailableDestinationsRequest request = new AvailableDestinationsRequest(city);
        postAndWait(request);

        assertInstanceOf(AvailableDestinationsResponse.class, event, "Expected an AvailableDestinationsResponse");
    }
}
