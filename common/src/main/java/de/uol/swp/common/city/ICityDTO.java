package de.uol.swp.common.city;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.infection.IInfectionDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Interface representing a City Data Transfer Object (DTO).
 */
public interface ICityDTO extends Serializable {

    /**
     * Gets the id of the city.
     *
     * @return the id of the city
     */
    int getId();

    /**
     * Gets the name of the city.
     *
     * @return the name of the city
     */
    String getName();

    /**
     * Gets the name of the plague associated with the city.
     *
     * @return the name of the plague
     */
    PlagueName getPlagueName();

    /**
     * Gets the foundation date of the city.
     *
     * @return the foundation date of the city
     */
    int getFoundationDate();

    /**
     * Checks if the city is a harbour city.
     *
     * @return true if the city is a harbour city, false otherwise
     */
    boolean isHarbourCity();

    /**
     * Checks if a hospital is built in the city.
     *
     * @return true if a hospital is built, false otherwise
     */
    boolean isHospitalBuild();

    /**
     * Gets the list of infections in the city.
     *
     * @return the list of infections
     */
    List<IInfectionDTO> getInfections();
}
