package de.uol.swp.server.connection;

import de.uol.swp.common.connection.request.AvailableDestinationsRequest;
import de.uol.swp.common.connection.response.AvailableDestinationsResponse;
import de.uol.swp.common.enums.PlagueName;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityMapper;
import de.uol.swp.server.city.CityName;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

// disabled sonar lint rule java:S5786, because public class modifier is required for EventBusBasedTest
public class ConnectionServiceTest extends EventBusBasedTest {

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
        City city = new City(29, PlagueName.YELLOW_FEVER, CityName.PALMA_DE_MALLORCA, -123, true);
        AvailableDestinationsRequest request = new AvailableDestinationsRequest(CityMapper.toDTO(city));
        postAndWait(request);

        assertInstanceOf(AvailableDestinationsResponse.class, event, "Expected an AvailableDestinationsResponse");
    }
}
