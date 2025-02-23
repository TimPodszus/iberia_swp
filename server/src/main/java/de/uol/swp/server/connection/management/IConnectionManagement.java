package de.uol.swp.server.connection.management;

import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.data.IPlayer;

import java.util.List;
import java.util.Map;

/**
 * Interface for managing connections.
 */
public interface IConnectionManagement {

    /**
     * Retrieves a list of available destinations from the given city for the current player.
     *
     * @param lobbyId the ID of the lobby
     * @param cityId  the ID of the city from which to get available destinations
     * @return a map of available destinations, and a list of cards that can be discarded to access them, if the list is
     * empty, the city is accessible without discarding a card
     */
    Map<ICity, List<ICard>> getAvailableDestinations(String lobbyId, int cityId);

    /**
     * Retrieves a list of available destinations for the given player.
     *
     * @param lobbyId the ID of the lobby
     * @param username  the username of the player for whom to get available destinations
     * @return a map of available destinations, and a list of cards that can be discarded to access them, if the list is
     * empty, the city is accessible without discarding a card
     */
    Map<ICity, List<ICard>> getAvailableDestinations(String lobbyId, String username);

    /**
     * Retrieves a map of all destinations from the given lobby.
     *
     * @param lobbyId the ID of the lobby
     * @return a map of all destinations and the list of cards that can be discarded to access them
     */
    Map<ICity, List<ICard>> getAllDestinations(String lobbyId);

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

    /**
     * Retrieves a game by its lobby ID.
     *
     * @param lobbyId the ID of the lobby
     * @return the game, or null if the lobby does not exist
     */
    IGame getGame(String lobbyId);
}
