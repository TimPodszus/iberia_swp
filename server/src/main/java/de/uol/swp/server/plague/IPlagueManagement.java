package de.uol.swp.server.plague;

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
    void researchPlague(PlagueName plagueToResearch, Game game) throws PlagueManagementException;

    /**
     * Checks if the cube count for a specific plague in the game is negative.
     *
     * @param game       the current game instance
     * @param plagueName the name of the plague to check
     * @return true if the cube count for the specified plague is negative, false otherwise
     */
    boolean isCubeCountNegative(IGame game, PlagueName plagueName);
}