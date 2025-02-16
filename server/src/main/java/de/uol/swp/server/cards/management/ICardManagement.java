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
}
