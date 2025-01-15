package de.uol.swp.server.game.management;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.usermanagement.IUser;

public interface IGameManagement {
    /**
     * Creates and initializes a new game based on the provided request.
     *
     * @param request the game creation request containing user and difficulty information
     * @return the newly created game
     */
    IGame createAndInitializeGame(CreateGameRequest request);

    /**
     * Sets the initial positioning of a player in the game based on the provided city.
     *
     * @param user      the user for whom the positioning is being set
     * @param lobbyCode the code of the lobby in which the game is happening
     * @param cityDTO   the city where the player will be positioned
     */
    void setPositioning(IUser user, String lobbyCode, CityDTO cityDTO);

    /**
     * Draws a player card. The specific behavior of this method should be defined.
     */
    InfectionCard drawInfectionCard(IGame game);

    /**
     * Adds an infection card to the infection card discard pile.
     */
    void discardInfectionCard(IGame game, InfectionCard infectionCard);
}

