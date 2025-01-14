package de.uol.swp.common.connection.response;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.PlagueName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for AvailableDestinationsResponse.
 */
public class AvailableDestinationsResponseTest {
    private static final Map<ICityDTO, Boolean> cities = Map.of(new CityDTO(
            1,
            PlagueName.CHOLERA,
            CityName.PALMA_DE_MALLORCA,
            1,
            true,
            false,
            new ArrayList<>()
    ), true);

    /**
     * Tests the AvailableDestinationsResponse constructor and getCities method.
     */
    @Test
    void testAvailableDestinationsResponse() {
        AvailableDestinationsResponse availableDestinationsResponse = new AvailableDestinationsResponse(cities);
        assertEquals(cities, availableDestinationsResponse.getCities());
    }
}
