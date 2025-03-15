package de.uol.swp.server.infection.management;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.infection.data.IInfection;

/**
 * Manages infections within the game.
 */
public class InfectionManagement extends AbstractManagement implements IInfectionManagement {

    /**
     * Finds an infection in a specific city and plague within a lobby.
     *
     * @param lobbyId    the ID of the lobby
     * @param cityName   the name of the city
     * @param plagueName the name of the plague
     * @return the infection found
     * @throws InfectionManagementException if the infection cannot be found
     */
    public IInfection findInfection(
            String lobbyId,
            CityName cityName,
            PlagueName plagueName
    ) throws InfectionManagementException {
        IGame game = getGame(lobbyId);
        CityRepository cityRepository = game.getCityRepository();
        ICity city = cityRepository.getCityByName(cityName);

        return city.getInfections()
                   .stream()
                   .filter(i -> i.getPlagueName()
                                 .equals(plagueName))
                   .findFirst()
                   .orElseThrow();
    }
}