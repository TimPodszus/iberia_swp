package de.uol.swp.server.infection.management;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.infection.data.IInfection;

public class InfectionManagement implements IInfectionManagement {
    public IInfection findInfection(ICity city, PlagueName plagueName) throws InfectionManagementException {
        return city.getInfections()
                   .stream()
                   .filter(i -> i.getPlague()
                                 .getName()
                                 .equals(plagueName))
                   .findFirst()
                   .orElseThrow(() -> new InfectionManagementException("Infection not found"));
    }
}
