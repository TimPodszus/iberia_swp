package de.uol.swp.server.infection.data;

import de.uol.swp.common.game.PlagueName;

public interface IInfection {

    /**
     * Sets the severity of the infection.
     *
     * @param severity the new severity of the infection
     */
    void setSeverity(int severity);

    /**
     * Returns the severity of the infection.
     *
     * @return the severity of the infection
     */
    int getSeverity();

    /**
     * Returns the plague associated with the infection.
     *
     * @return the plague associated with the infection
     */
    PlagueName getPlagueName();

    /**
     * Decreases the severity of the infection by the specified amount.
     *
     * @param amount the amount by which to decrease the severity
     */
    void decreaseSeverity(int amount);
}
