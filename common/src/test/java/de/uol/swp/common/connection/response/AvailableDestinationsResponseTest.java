package de.uol.swp.common.connection.response;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.ICityDTO;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for AvailableDestinationsResponse.
 */
public class AvailableDestinationsResponseTest {
    private static final Map<ICityDTO, Boolean> cities = Map.of(
            new CityDTO(1, "city1", "testcity", 1, true, false),
            true
    );

    /**
     * Tests the AvailableDestinationsResponse constructor and getCities method.
     */
    @Test
    void testAvailableDestinationsResponse() {
        AvailableDestinationsResponse availableDestinationsResponse = new AvailableDestinationsResponse(cities);
        assertEquals(cities, availableDestinationsResponse.getCities());
    }
}
