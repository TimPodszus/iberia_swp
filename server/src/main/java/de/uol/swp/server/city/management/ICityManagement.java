package de.uol.swp.server.city.management;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;

public interface ICityManagement {

    void infectCity(
            IGame game,
            InfectionCard infectionCard,
            PlagueName plagueName,
            int amount
    ) throws CityManagementException;

    ICity getCity(String lobbyId, String cityId);

    void infectCityWithOwnPlague(IGame game, InfectionCard infectionCard, int amount);
}
