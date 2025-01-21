package de.uol.swp.server.game.management;

import de.uol.swp.common.game.GameActions;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.usermanagement.IUser;

import java.util.List;

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
    IGame setPositioning(PositioningRequest request) throws GameManagementException, PlayerManagementException;

    /**
     * Draws a player card. The specific behavior of this method should be defined.
     */
    InfectionCard drawInfectionCard(IGame game);

    /**
     * Adds an infection card to the infection card discard pile.
     */
    void discardInfectionCard(IGame game, InfectionCard infectionCard);

    /**
     * Retrieves the list of available actions for a given lobby.
     *
     * @param lobbyId the ID of the lobby for which to retrieve available actions
     * @return a list of available game actions
     */
    List<GameActions> getAvailableActions(String lobbyId, IUser user);
}

