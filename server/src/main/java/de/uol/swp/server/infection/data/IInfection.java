package de.uol.swp.server.infection.data;

import de.uol.swp.server.plague.data.IPlague;

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
    IPlague getPlague();
}
