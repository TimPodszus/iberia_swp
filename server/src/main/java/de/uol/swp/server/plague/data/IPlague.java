package de.uol.swp.server.plague.data;

import de.uol.swp.common.game.PlagueName;

/**
 * Interface representing a plague in the game.
 */
public interface IPlague {

    /**
     * Gets the name of the plague.
     *
     * @return the name of the plague
     */
    PlagueName getName();

    /**
     * Gets the number of cubes remaining for the plague.
     *
     * @return the number of cubes remaining for the plague
     */
    int getCubesRemaining();

    /**
     * Sets the number of cubes remaining for the plague.
     *
     * @param cubesRemaining the number of cubes remaining to set
     */
    void setCubesRemaining(int cubesRemaining);

    /**
     * Checks if the plague has been researched.
     *
     * @return true if the plague has been researched, false otherwise
     */
    boolean isResearched();

    /**
     * Sets the researched status of the plague.
     *
     * @param researched the researched status to set
     */
    void setResearched(boolean researched);

    /**
     * Increases the number of cubes remaining for the plague by the specified amount.
     *
     * @param amount the amount by which to increase the number of cubes
     */
    void increaseCubes(int amount);
}
