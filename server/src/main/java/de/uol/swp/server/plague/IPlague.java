package de.uol.swp.server.plague;

import de.uol.swp.common.game.PlagueName;

public interface IPlague {
    /**
     * Gets the name of the plague.
     *
     * @return the name
     */
    PlagueName getName();

    /**
     * Gets the remaining cubes of the plague.
     *
     * @return the remaining cubes
     */
    int getCubesRemaining();

    /**
     * Sets the remaining cubes of the plague.
     *
     * @param cubesRemaining the new remaining cubes
     */
    void setCubesRemaining(int cubesRemaining);

    /**
     * Gets the researched state of the plague.
     *
     * @return the researched state
     */
    boolean isResearched();

    /**
     * Sets the researched state of the plague.
     *
     * @param researched the new researched state
     */
    void setResearched(boolean researched);
}
