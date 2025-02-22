package de.uol.swp.server.infection.management;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.infection.data.IInfection;

public class InfectionManagement extends AbstractManagement implements IInfectionManagement {

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
