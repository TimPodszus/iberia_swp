package de.uol.swp.server.connection.management;

import de.uol.swp.server.city.City;

import java.util.Map;

/**
 * Interface for managing connections.
 */
public interface IConnectionManagement {

    /**
     * Retrieves a list of available destinations from the given city.
     *
     * @param lobbyId the ID of the lobby
     * @param cityId  the city from which to get available destinations
     * @return a map of available destinations, and whether they are only accessible when the player discards the city card
     */
    Map<City, Boolean> getAvailableDestinations(String lobbyId, String cityId);
}
