package de.uol.swp.server.connection.management;

import de.uol.swp.server.cards.Card;
import de.uol.swp.server.city.data.ICity;

import java.util.List;
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
     * @return a map of available destinations, and a list of cards that can be discarded to access them, if the list is
     * empty, the city is accessible without discarding a card
     */
    Map<ICity, List<Card>> getAvailableDestinations(String lobbyId, int cityId);
}
