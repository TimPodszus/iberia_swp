package de.uol.swp.server.game.management;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.game.data.Game;

public interface IGameManagement {
    /**
     * Creates and initializes a new game based on the provided request.
     *
     * @param request the game creation request containing user and difficulty information
     * @return the newly created game
     */
    Game createAndInitializeGame(CreateGameRequest request);

    /**
     * Sets the initial positioning of a player in the game based on the provided city.
     *
     * @param user the user for whom the positioning is being set
     * @param lobbyCode the code of the lobby in which the game is happening
     * @param cityDTO the city where the player will be positioned
     */
    void setPositioning(User user, String lobbyCode, CityDTO cityDTO);

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
}

