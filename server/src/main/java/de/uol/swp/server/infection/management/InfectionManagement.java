package de.uol.swp.server.infection.management;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.infection.data.Infection;
import de.uol.swp.server.plague.data.IPlague;
import de.uol.swp.server.plague.data.Plague;
import de.uol.swp.server.plague.data.PlagueRepository;

import java.util.Optional;

public class InfectionManagement implements IInfectionManagement {

    public IInfection findInfection(ICity city, PlagueName plagueName) throws InfectionManagementException {
        PlagueRepository plagueRepository = new PlagueRepository();

        Optional<IInfection> existingInfection = city.getInfections()
                                                     .stream()
                                                     .filter(i -> i.getPlagueName()
                                                                   .equals(plagueName))
                                                     .findFirst();

        if (existingInfection.isEmpty()) {
            IPlague plague = plagueRepository.getPlagueByName(plagueName);
            if (plague != null) {
                Infection newInfection = new Infection(0, plague.getName());
                city.getInfections()
                    .add(newInfection);
                return newInfection;
            } else {
                throw new InfectionManagementException("Plague " + plagueName + " not found");
            }
        }
        return existingInfection.get();
    }
}
