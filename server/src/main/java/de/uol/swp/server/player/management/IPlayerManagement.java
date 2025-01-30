package de.uol.swp.server.player.management;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.server.cards.ICard;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.usermanagement.IUser;

import java.util.List;

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
    ICardDTO drawPlayerCard(String lobbyCode, IUser user) throws PlayerManagementException;

    /**
     * Draws a player card for a given player in a game.
     *
     * @param game   the game instance
     * @param player the player drawing the card
     * @return the drawn card
     * @throws PlayerManagementException if an error occurs while drawing the card
     */
    ICardDTO drawPlayerCard(String lobbyCode, IPlayer player) throws PlayerManagementException;

    /**
     * Sets the starting position for a player in a specified city.
     *
     * @param cityName the name of the city
     * @param player   the player whose starting position is being set
     */
    void setStartingPosition(String lobbyCode, CityName cityName, IPlayer player) throws PlayerManagementException;

    /**
     * Adds a card to the player's hand.
     *
     * @param player the player to whom the card is being added
     * @param card   the card to be added
     */
    void addCard(IPlayer player, ICard card);

    /**
     * Discards a single card from the player's hand.
     *
     * @param player the player discarding the card
     * @param card   the card to be discarded
     * @param <T>    the type of the card, extending ICard
     */
    <T extends ICard> void discardCard(String lobbyCode, IPlayer player, T card);

    /**
     * Discards multiple cards from the player's hand.
     *
     * @param player the player discarding the cards
     * @param cards  the list of cards to be discarded
     */
    void discardCards(String lobbyCode, IPlayer player, List<? extends ICard> cards);
}