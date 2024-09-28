package de.uol.swp.common.city;

import java.io.Serializable;

/**
 * Interface representing a City Data Transfer Object (DTO).
 */
public interface ICityDTO extends Serializable {

    /**
     * Gets the ID of the city.
     *
     * @return the ID of the city
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
    String getPlagueName();

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
}
