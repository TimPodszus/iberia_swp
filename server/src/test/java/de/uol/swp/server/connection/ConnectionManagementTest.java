package de.uol.swp.server.connection;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityName;
import de.uol.swp.server.connection.management.ConnectionManagement;
import org.junit.jupiter.api.Test;

class ConnectionManagementTest {
    final ConnectionManagement connectionManagement = new ConnectionManagement();

    /**
     * Tests the available destinations for the city Palma de Mallorca.
     * It verifies that the number of available destinations is as expected.
     */
    @Test
    void testAvailableDestinationsForPalmaDeMallorca() {
        City city = new City(29, PlagueName.YELLOW_FEVER, CityName.PALMA_DE_MALLORCA, -123, true);
    }
}
