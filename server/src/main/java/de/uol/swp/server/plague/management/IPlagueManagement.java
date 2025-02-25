package de.uol.swp.server.plague.management;

import de.uol.swp.server.game.data.IGame;

public interface IPlagueManagement {
    /**
     * Researches a plague in the game if the player meets the necessary conditions.
     *
     * @param game the current game instance
     * @throws PlagueManagementException if the plague cannot be researched due to game conditions
     */
    void researchPlague(IGame game) throws PlagueManagementException;

    boolean canResearchPlague(IGame game);
    IGame getGame(String lobbyId);
}