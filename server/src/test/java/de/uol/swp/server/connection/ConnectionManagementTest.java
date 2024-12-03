package de.uol.swp.server.connection;

import de.uol.swp.common.enums.PlagueName;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityName;
import de.uol.swp.server.connection.management.ConnectionManagement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConnectionManagementTest {
    final ConnectionManagement connectionManagement = new ConnectionManagement();

    /**
     * Tests the available destinations for the city Palma de Mallorca.
     * It verifies that the number of available destinations is as expected.
     */
    @Test
    void testAvailableDestinationsForPalmaDeMallorca() {
        City city = new City(29, PlagueName.YELLOW_FEVER, CityName.PALMA_DE_MALLORCA, -123, true);

        List<City> destinations = connectionManagement.getAvailableDestinations(city);

        assertEquals(2, destinations.size(), "Expected 2 available destinations for Palma de Mallorca");
    }
}
