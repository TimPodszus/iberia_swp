package de.uol.swp.server.infection.management;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.infection.data.IInfection;

/**
 * Interface for managing infections within the game.
 */
public interface IInfectionManagement {

    /**
     * Finds an infection in a specific city and plague within a lobby.
     *
     * @param lobbyId    the ID of the lobby
     * @param cityName   the name of the city
     * @param plagueName the name of the plague
     * @return the infection found
     * @throws InfectionManagementException if the infection cannot be found
     */
    IInfection findInfection(
            String lobbyId,
            CityName cityName,
            PlagueName plagueName
    ) throws InfectionManagementException;
}