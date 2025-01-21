package de.uol.swp.server.region.management;

import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;

/**
 * Interface for managing regions in the game.
 */
public interface IRegionManagement {

    /**
     * Reduces the water treatments in the regions associated with the specified city.
     *
     * @param game   the game instance
     * @param city   the city whose regions' water treatments are to be reduced
     * @param amount the amount of water treatments to reduce
     * @return the remaining amount of water treatments to be reduced if the regions do not have enough
     * @throws RegionManagementException if an error occurs during the reduction process
     */
    int reduceWaterTreatments(IGame game, ICity city, int amount) throws RegionManagementException;
}