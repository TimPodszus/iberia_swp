package de.uol.swp.common.plague;

import de.uol.swp.common.game.PlagueName;

public interface IPlagueDTO {
    /**
     * Gets the name.
     *
     * @return the name
     */
    PlagueName getName();

    /**
     * Gets the cubes remaining.
     *
     * @return the cubes remaining
     */
    int getCubesRemaining();

    /**
     * Gets the researched.
     *
     * @return the researched
     */
    boolean isResearched();
}
