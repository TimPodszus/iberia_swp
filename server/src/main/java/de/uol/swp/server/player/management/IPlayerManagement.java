package de.uol.swp.server.player.management;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.usermanagement.IUser;

/**
 * Interface for managing player-related operations.
 */
public interface IPlayerManagement {

    /**
     * Draws a player card for a given user in a game.
     *
     * @param game the game instance
     * @param user the user drawing the card
     * @return the drawn card
     * @throws PlayerManagementException if an error occurs while drawing the card
     */
    ICardDTO drawPlayerCard(IGame game, IUser user) throws PlayerManagementException;

    /**
     * Draws a player card for a given player in a game.
     *
     * @param game   the game instance
     * @param player the player drawing the card
     * @return the drawn card
     * @throws PlayerManagementException if an error occurs while drawing the card
     */
    ICardDTO drawPlayerCard(IGame game, IPlayer player) throws PlayerManagementException;

    /**
     * Sets the starting position for a player in a specified city.
     *
     * @param cityName the name of the city
     * @param player   the player whose starting position is being set
     */
    void setStartingPosition(CityName cityName, IPlayer player) throws PlayerManagementException;
}