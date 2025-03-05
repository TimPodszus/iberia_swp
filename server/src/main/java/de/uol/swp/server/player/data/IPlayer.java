package de.uol.swp.server.player.data;

import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.player.PositionChangeListener;
import de.uol.swp.server.role.IRole;
import de.uol.swp.server.usermanagement.IUser;

import java.util.List;

public interface IPlayer {
    /**
     * Gets the role of the player.
     *
     * @return the role of the player
     */
    IRole getRole();

    /**
     * Sets the role of the player.
     *
     * @param role the role to set
     */
    void setRole(IRole role);

    /**
     * Gets the current position of the player.
     *
     * @return the current position of the player
     */
    ICity getCurrentPosition();

    /**
     * Sets the current position of the player.
     *
     * @param city the city to set as the current position
     */
    void setCurrentPosition(ICity city);

    /**
     * Gets the cards of the player.
     *
     * @return the list of cards
     */
    List<ICard> getCards();

    /**
     * Sets the cards of the player.
     *
     * @param cards the list of cards to set
     */
    void setCards(List<ICard> cards);

    /**
     * Gets the user associated with the player.
     *
     * @return the user
     */
    IUser getUser();

    /**
     * Plays the card with the specified ID from the players hand.
     *
     * @param cardId the ID of the card which should be played
     * @return the played card or null if the card is not in the player's hand
     */
    ICard playCard(int cardId);

    /**
     * Gets the card with the specified ID from the players hand.
     *
     * @param cardId the ID of the card to get
     * @return the card or null if the card is not in the player's hand
     */
    ICard getCard(int cardId);

    /**
     * Gets the game ID of the player.
     *
     * @return the game ID
     */
    String getGameId();

    /**
     * Sets the position change listener for the player.
     *
     * @param positionChangeListener the listener to set
     */
    void setPositionChangeListener(PositionChangeListener positionChangeListener);
}
