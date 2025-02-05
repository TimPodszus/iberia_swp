package de.uol.swp.server.region.data;

import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.region.management.RegionManagementException;

import java.util.List;

/**
 * Interface representing a region in the game.
 */
public interface IRegion {

    /**
     * Gets the unique identifier of the region.
     *
     * @return the unique identifier of the region
     */
    int getId();

    /**
     * Gets the number of water treatments available in the region.
     *
     * @return the number of water treatments available in the region
     */
    int getWaterTreatments();

    /**
     * Increases the number of water treatments in the region by the specified count.
     *
     * @param amount the amount by which to increase the water treatments
     */
    void increaseWaterTreatments(int amount);

    /**
     * Decreases the number of water treatments in the region by the specified amount.
     *
     * @param amount the amount by which to decrease the water treatments
     * @throws RegionManagementException if the amount to decrease is greater than the available water treatments
     */
    void decreaseWaterTreatments(int amount) throws RegionManagementException;

    /**
     * Gets the list of names of the surrounding cities.
     *
     * @return the list of names of the surrounding cities
     */
    List<ICity> getSurroundingCities();

    /**
     * Checks if the region has a prevention marker.
     *
     * @return true if the region has a prevention marker, false otherwise
     */
    boolean isPreventionMarker();
}