package de.uol.swp.server.player.data;

import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
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
    List<Card> getCards();

    /**
     * Sets the cards of the player.
     *
     * @param cards the list of cards to set
     */
    void setCards(List<Card> cards);

    /**
     * Gets the user associated with the player.
     *
     * @return the user
     */
    IUser getUser();

    /**
     * Gets the city repository.
     *
     * @return the city repository
     */
    CityRepository getCityRepository();

    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to add
     */
    void addCard(Card card);

    /**
     * Plays a card from the player's hand.
     *
     * @param card the card to play
     */
    void playCard(Card card);

    /**
     * Discards a card from the player's hand.
     *
     * @param card the card to discard
     */
    void discardCard(Card card);

    /**
     * Gets the city card of the player. Returns null if the player does not have the city card.
     *
     * @param city the city to get the card for
     * @return the city card or null, if the player does not have the card
     */
    CityCard getCityCard(ICity city);
}
