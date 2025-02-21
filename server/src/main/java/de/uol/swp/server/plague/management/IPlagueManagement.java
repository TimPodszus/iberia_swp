package de.uol.swp.server.plague.management;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;

public interface IPlagueManagement {
    /**
     * Researches a plague in the game if the player meets the necessary conditions.
     *
     * @param plagueToResearch the plague to be researched
     * @param game             the current game instance
     * @throws PlagueManagementException if the plague cannot be researched due to game conditions
     */
    void researchPlague(PlagueName plagueToResearch, IGame game) throws PlagueManagementException;
    boolean canResearchPlague(PlagueName plagueName, IGame game);
}