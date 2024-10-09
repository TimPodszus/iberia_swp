package de.uol.swp.server.infection;

import de.uol.swp.server.plague.Plague;

public interface IInfection {
    /**
     * Gets the severity of the infection.
     *
     * @return the severity
     */
    int getSeverity();

    /**
     * Sets the severity of the infection.
     *
     * @param severity the new severity
     */
    void setSeverity(int severity);

    /**
     * Gets the plague.
     *
     * @return the plague
     */
    Plague getPlague();
}
