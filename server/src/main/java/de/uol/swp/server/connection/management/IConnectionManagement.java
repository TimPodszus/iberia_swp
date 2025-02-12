package de.uol.swp.server.connection.management;

import de.uol.swp.server.cards.ICard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.connection.data.IConnection;

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
    Map<ICity, List<ICard>> getAvailableDestinations(String lobbyId, int cityId);

    /**
     * Retrieves a list of buildable train tracks from the given city.
     *
     * @param lobbyId the ID of the lobby
     * @param cityId  the city from which to get buildable train tracks
     * @return a list of buildable train tracks
     */
    List<IConnection> getBuildableTrainTracks(String lobbyId, int cityId);

}
