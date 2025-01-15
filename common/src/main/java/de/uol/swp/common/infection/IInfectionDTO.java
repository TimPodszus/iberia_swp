package de.uol.swp.common.infection;

import de.uol.swp.common.plague.IPlagueDTO;

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
    IPlagueDTO getPlague();
}
