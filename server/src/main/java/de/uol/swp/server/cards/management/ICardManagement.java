package de.uol.swp.server.cards.management;

import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;

import java.util.List;

/**
 * Interface for managing card-related actions in the game.
 */
public interface ICardManagement {

    /**
     * Plays a card in the specified lobby.
     *
     * @param lobbyId  the ID of the lobby where the card is played
     * @param username the username of the player playing the card
     * @param cardId   the ID of the card being played
     * @throws CardNotPlayableException if the card is not playable
     */
    void playCard(String lobbyId, String username, int cardId) throws CardNotPlayableException;

    /**
     * Retrieves the game associated with the specified lobby.
     *
     * @param lobbyId the ID of the lobby
     * @return the game associated with the specified lobby
     */
    IGame getGame(String lobbyId);

    /**
     * Plays a second chance card in the specified lobby.
     *
     * @param lobbyId  the ID of the lobby where the card is played
     * @param username the username of the player playing the card
     * @throws CardNotFoundException if the card is not found
     */
    void playSecondChanceCard(String lobbyId, String username) throws CardNotFoundException;

    /**
     * Returns the last played card back to players hand.
     *
     * @param lobbyId  the ID of the lobby
     * @param username the username of the player returning the card
     */
    void returnLastPlayedCard(String lobbyId, String username);

    /**
     * Checks if the card is playable.
     *
     * @param game     the game instance to check
     * @param cardId   the id of the card to check
     * @param username the username of the player
     * @return true if the card is playable, false otherwise
     */
    boolean isCardPlayable(IGame game, int cardId, String username);

    /**
     * Checks if the state of the game is correct.
     *
     * @param game the game instance to check
     * @return true if the state is correct, false otherwise
     */
    boolean isStateCorrect(IGame game);

    /**
     * Retrieves the cards from the player's discard pile in the specified lobby.
     *
     * @param lobbyId the ID of the lobby
     * @param type    the type of card to retrieve
     * @return a list of cards from the player's discard pile with the given type or every card, when type is null
     */
    <T extends ICard> List<T> getCardsFromPlayerDiscardPile(String lobbyId, Class<T> type);

    /**
     * Retrieves the card with the specified ID from the discard pile and add it to users hand.
     * Checks if the game is in the correct state, and the player is able to retrieve the card from the discard pile.
     *
     * @param lobbyId  the ID of the lobby
     * @param username the username of the player
     * @param cardId   the ID of the card
     * @throws IllegalGameStateException if the game is not in the correct state
     * @throws CardNotFoundException     if the card is not found
     */
    void getCardForPlayer(
            String lobbyId, String username, int cardId
    ) throws CardNotFoundException, IllegalGameStateException;
}
