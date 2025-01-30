package de.uol.swp.server.connection.data;


import de.uol.swp.common.city.CityName;

import java.util.List;

/**
 * Interface representing a connection.
 */
public interface IConnection {

    /**
     * Gets the ID of the connection.
     *
     * @return the ID of the connection
     */
    int getId();

    /**
     * Gets the list of city names associated with the connection.
     *
     * @return the list of city names
     */
    List<CityName> getCityNames();

    /**
     * Checks if the connection is a train track.
     *
     * @return true if it is a train track, false otherwise
     */
    boolean isTrainTrack();

    /**
     * Checks if the train track is buildable.
     *
     * @return true if the train track is buildable, false otherwise
     */
    boolean isTrainTrackBuildable();

    /**
     * Builds the train tracks for the connection.
     */
    void buildTrainTracks(boolean trainTrack);
}
