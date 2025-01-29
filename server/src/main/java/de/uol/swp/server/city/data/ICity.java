package de.uol.swp.server.city.data;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.infection.data.IInfection;

import java.util.List;

/**
 * Interface representing a city in the game.
 */
public interface ICity {

    /**
     * Gets the unique identifier of the city.
     *
     * @return the unique identifier of the city
     */
    int getId();

    /**
     * Gets the plague name associated with the city.
     *
     * @return the plague name associated with the city
     */
    PlagueName getPlagueName();

    /**
     * Gets the name of the city.
     *
     * @return the name of the city
     */
    CityName getName();

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
    boolean isHospitalBuilt();

    /**
     * Sets the hospital built status of the city.
     *
     * @param hospitalBuilt the hospital built status to set
     */
    void setHospitalBuilt(boolean hospitalBuilt);

    /**
     * Gets the list of infections in the city.
     *
     * @return the list of infections in the city
     */
    List<IInfection> getInfections();

    /**
     * Sets the list of infections in the city.
     *
     * @param infections the list of infections to set
     */
    void setInfections(List<IInfection> infections);

    /**
     * Gets the color of the city.
     *
     * @return the color of the city.
     */
    String getColor();
}