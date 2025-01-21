package de.uol.swp.server.infection.management;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.infection.data.IInfection;

public interface IInfectionManagement {
    IInfection findInfection(ICity city, PlagueName plagueName) throws InfectionManagementException;
}
