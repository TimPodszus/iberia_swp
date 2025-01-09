package de.uol.swp.server.city.management;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.game.data.IGame;

public interface ICityManagement {

    void infectCity(
            IGame game,
            InfectionCard infectionCard,
            PlagueName plagueName,
            int amount
    ) throws CityManagementException;

    void infectCityWithOneCube(IGame game, InfectionCard infectionCard, PlagueName plagueName);

    void infectCityWithOwnPlague(IGame game, InfectionCard infectionCard, int amount);

    void infectCityWithOneCubeAndOwnPlague(IGame game, InfectionCard infectionCard);
}
