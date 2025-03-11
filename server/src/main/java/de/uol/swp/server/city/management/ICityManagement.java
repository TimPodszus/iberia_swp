package de.uol.swp.server.city.management;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;

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
            IGame game,
            InfectionCard infectionCard,
            PlagueName plagueName,
            int amount
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

    /**
     * Builds a hospital in the specified city.
     *
     * @param lobbyId  the ID of the lobby where the hospital is to be built
     * @param userName the name of the user requesting the hospital build
     * @param cityId   the ID of the city where the hospital is to be built
     */
    void buildHospital(String lobbyId, String userName, Integer cityId) throws CityManagementException, GameException;

    /**
     * Builds a hospital in the specified city using an event card.
     *
     * @param lobbyId  the ID of the lobby where the hospital is to be built
     * @param cityId   the ID of the city where the hospital is to be built
     * @param userName the name of the user requesting the hospital build
     */
    void buildHospitalWithEventCard(String lobbyId, Integer cityId, String userName);

    /**
     * Checks if a hospital can be built in the specified city by the specified user.
     *
     * @param lobbyCode the code of the lobby
     * @param username  the name of the user
     * @return true if the hospital can be built, false otherwise
     */
    boolean isHospitalBuildable(String lobbyCode, String username);
}
