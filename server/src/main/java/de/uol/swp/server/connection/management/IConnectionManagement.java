package de.uol.swp.server.connection.management;

import de.uol.swp.common.connection.dto.DestinationInfo;
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
     * @return a map of available destinations as cityIds, and an object with list of cards that can be discarded to
     * access them,
     * and a flag indicating the way to access the city
     */
    Map<Integer, DestinationInfo> getAvailableDestinations(String lobbyId, int cityId);

    /**
     * Retrieves a map of all destinations from the given lobby.
     *
     * @param lobbyId the ID of the lobby
     * @return a map of all destinations as cityIds, and an object with list of cards that can be discarded to access
     * them,
     * and a flag indicating the way to access the city
     */
    Map<Integer, DestinationInfo> getAllDestinations(String lobbyId);

    /**
     * Retrieves a list of buildable train tracks from the given city.
     *
     * @param lobbyId the ID of the lobby
     * @param cityId  the city from which to get buildable train tracks
     * @return a list of buildable train tracks
     */
    List<IConnection> getBuildableTrainTracks(String lobbyId, int cityId);

    /**
     * Retrieves a connection by its ID.
     *
     * @param lobbyId      the ID of the lobby
     * @param connectionId the ID of the connection
     * @return the connection
     */
    IConnection getConnection(String lobbyId, int connectionId);
}
