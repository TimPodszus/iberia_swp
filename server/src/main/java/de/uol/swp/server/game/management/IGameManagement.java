package de.uol.swp.server.game.management;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.City;
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
    void drawPlayerCard();

    /**
     * Draws an infection card from the deck.
     *
     * @return the drawn infection card
     */
    InfectionCard drawInfectionCard();

    /**
     * Moves a player to a specified city in the game.
     *
     * @param user      the user representing the player to be moved
     * @param lobbyCode the code of the lobby in which the game is happening
     * @param cityId    the cityId to which the player will be moved
     *                  <p>
     * @throws GameManagementException if moving the player fails
     */
    void movePlayer(IUser user, String lobbyCode, City cityId) throws GameManagementException;
}

