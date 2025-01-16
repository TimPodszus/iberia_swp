package de.uol.swp.server.game.management;

import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.management.PlayerManagementException;

public interface IGameManagement {
    /**
     * Creates and initializes a new game based on the provided request.
     *
     * @param request the game creation request containing user and difficulty information
     * @return the newly created game
     */
    IGame createAndInitializeGame(CreateGameRequest request) throws PlayerManagementException;

    /**
     * Sets the initial positioning of a player in the game based on the provided city.
     *
     * @param request      The request with where the position is to be set
     */
    IGame setPositioning(PositioningRequest request) throws GameManagementException;

    /**
     * Draws a player card. The specific behavior of this method should be defined.
     */
    InfectionCard drawInfectionCard(IGame game);

    /**
     * Adds an infection card to the infection card discard pile.
     */
    void discardInfectionCard(IGame game, InfectionCard infectionCard);
}

