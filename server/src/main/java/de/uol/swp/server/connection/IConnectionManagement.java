package de.uol.swp.server.connection;

import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.server.city.City;

import java.util.List;

/**
 * Interface for managing connections.
 */
public interface IConnectionManagement {

    /**
     * Retrieves a list of available destinations from the given city.
     *
     * @param city the city from which to get available destinations
     * @return a list of available destinations
     */
    List<City> getAvailableDestinations(City city);
}
