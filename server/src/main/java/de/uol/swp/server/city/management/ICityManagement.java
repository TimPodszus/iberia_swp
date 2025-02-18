package de.uol.swp.server.city.management;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;

/**
 * Interface for managing city-related operations in the game.
 */
public interface ICityManagement {

    /**
     * Infects a city with a specified plague.
     *
     * @param game          the game instance
     * @param infectionCard the infection card used
     * @param plagueName    the name of the plague
     * @param amount        the amount of infection
     * @throws CityManagementException if an error occurs during infection
     */
    void infectCity(
            IGame game, InfectionCard infectionCard, PlagueName plagueName, int amount
    ) throws CityManagementException;

    /**
     * Retrieves a city by its lobby ID and city ID.
     *
     * @param lobbyId the lobby ID
     * @param cityId  the city ID
     * @return the city instance
     */
    ICity getCity(String lobbyId, int cityId);

    /**
     * Infects a city with its own plague.
     *
     * @param game          the game instance
     * @param infectionCard the infection card used
     * @param amount        the amount of infection
     */
    void infectCityWithOwnPlague(IGame game, InfectionCard infectionCard, int amount);

    void buildHospital(String lobbyId, String userName, CityName cityName);

    void buildHospitalWithEventCard(String lobbyId, CityName targetCityName);
}
