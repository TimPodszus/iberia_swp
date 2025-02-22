package de.uol.swp.server.infection.management;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.infection.data.IInfection;

public interface IInfectionManagement {
    IInfection findInfection(
            String lobbyId,
            CityName cityName,
            PlagueName plagueName
    ) throws InfectionManagementException;
}
