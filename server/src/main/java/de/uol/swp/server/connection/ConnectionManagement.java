package de.uol.swp.server.connection;

import de.uol.swp.server.city.City;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages connections and provides available destinations.
 */
@AllArgsConstructor
public class ConnectionManagement implements IConnectionManagement {

    @Override
    public List<City> getAvailableDestinations(City city) {
        List<City> availableDestinations = new ArrayList<>();
        for (Connection connection : ConnectionRepository.getAllConnections()) {
            if (connection.getCities()
                          .contains(city)) {
                for (City connectedCity : connection.getCities()) {
                    if (!connectedCity.equals(city)) {

                        availableDestinations.add(connectedCity);
                    }
                }
            }
        }
        return availableDestinations;
    }
}
