package de.uol.swp.server.cards.management;

import de.uol.swp.server.game.data.IGame;

/**
 * Interface for managing card-related actions in the game.
 */
public interface ICardManagement {

    /**
     * Plays a card in the specified lobby.
     *
     * @param lobbyId the ID of the lobby where the card is played
     * @param username the username of the player playing the card
     * @param cardId the ID of the card being played
     */
    void playCard(String lobbyId, String username, int cardId);

    /**
     * Retrieves the game associated with the specified lobby.
     *
     * @param lobbyId the ID of the lobby
     * @return the game associated with the specified lobby
     */
    IGame getGame(String lobbyId);

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

}
