package de.uol.swp.server.connection.data;

import de.uol.swp.common.city.CityName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Represents a connection between cities in the game.
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class Connection implements IConnection {
    /**
     * The unique identifier for the connection.
     */
    private final int id;

    /**
     * The list of city names that are connected.
     */
    private final List<CityName> cityNames;

    /**
     * Indicates if a train track is present on the connection.
     */
    private boolean trainTrack;

    /**
     * Indicates if a train track can be built on the connection.
     */
    private final boolean trainTrackBuildable;

    @Override
    public void buildTrainTracks(boolean trainTrack) {
        this.trainTrack = trainTrack;
    }
}