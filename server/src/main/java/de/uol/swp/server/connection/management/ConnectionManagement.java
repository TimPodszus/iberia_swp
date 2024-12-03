package de.uol.swp.server.connection.management;

import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityName;
import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.connection.data.IConnection;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages connections and provides available destinations.
 */
@AllArgsConstructor
public class ConnectionManagement extends AbstractManagement implements IConnectionManagement {

    @Override
    public List<City> getAvailableDestinations(String lobbyId, String cityId) {
        CityRepository cityRepository = super.getGame(lobbyId)
                                             .getCityRepository();
        ConnectionRepository connectionRepository = super.getGame(lobbyId)
                                                         .getConnectionRepository();
        City startCity = cityRepository.getCity(cityId);

        List<City> availableDestinations = new ArrayList<>();
        for (IConnection connection : connectionRepository.getConnections()) {
            if (connection.getCityNames()
                          .contains(startCity.getName())) {
                for (CityName connectedCity : connection.getCityNames()) {
                    if (!connectedCity.equals(startCity.getName())) {
                        City city = super.getGame(lobbyId)
                                         .getCityRepository()
                                         .getCitiesByNames(connectedCity)
                                         .get(0);
                        availableDestinations.add(city);
                    }
                }
            }
        }
        return availableDestinations;
    }
}
