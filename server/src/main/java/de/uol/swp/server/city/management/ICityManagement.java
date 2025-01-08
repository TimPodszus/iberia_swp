package de.uol.swp.server.city.management;

import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.City;

public interface ICityManagement {

    void infectCity(InfectionCard infectionCard, int amount);

    City getCity(String lobbyId, String cityId);

}
