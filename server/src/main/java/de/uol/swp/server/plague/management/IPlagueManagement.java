package de.uol.swp.server.plague.management;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.infection.data.IInfection;

import java.util.List;

public interface IPlagueManagement {
    /**
     * Researches a plague in the game if the player meets the necessary conditions.
     *
     * @param plagueToResearch the plague to be researched
     * @param game             the current game instance
     * @throws PlagueManagementException if the plague cannot be researched due to game conditions
     */
    void researchPlague(PlagueName plagueToResearch, Game game) throws PlagueManagementException;

    IGame getGame(String lobbyId);

    List<IInfection> getInfectionsInCity(IGame game);

    List<ICity> getCitiesNearBy(IGame game, ICity currentCity);
}