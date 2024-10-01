package de.uol.swp.server.connection;

import de.uol.swp.server.city.CityName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents a connection between cities in the game.
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class Connection {
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
    @Setter
    private boolean trainTrack;

    /**
     * Indicates if a train track can be built on the connection.
     */
    private final boolean trainTrackBuildable;
}