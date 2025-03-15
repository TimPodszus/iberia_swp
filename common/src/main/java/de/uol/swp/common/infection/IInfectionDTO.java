package de.uol.swp.common.infection;

import de.uol.swp.common.game.PlagueName;

public interface IInfectionDTO {
    /**
     * Gets the severity.
     *
     * @return the severity
     */
    int getSeverity();

    /**
     * Gets the plague.
     *
     * @return the plague
     */
    PlagueName getPlagueName();
}
