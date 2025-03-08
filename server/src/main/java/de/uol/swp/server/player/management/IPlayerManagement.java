package de.uol.swp.server.player.management;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.player.data.CardsAmountChangeListener;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.usermanagement.IUser;

import java.util.List;

/**
 * Interface for managing player-related operations.
 */
public interface IPlayerManagement {

    /**
     * Sets the GameStateChangeListener.
     *
     * @param listener the new GameStateChangeListener
     */
    void setCardsAmountChangeListener(CardsAmountChangeListener listener);

    /**
     * Draws a player card for a given user in a game.
     *
     * @param lobbyCode the code of the lobby
     * @param user      the user drawing the card
     * @return the drawn card
     * @throws PlayerManagementException if an error occurs while drawing the card
     */
    ICardDTO drawPlayerCard(String lobbyCode, IUser user) throws PlayerManagementException;

    /**
     * Draws a player card for a given player in a game.
     *
     * @param lobbyCode the code of the lobby
     * @param player    the player drawing the card
     * @return the drawn card
     * @throws PlayerManagementException if an error occurs while drawing the card
     */
    ICardDTO drawPlayerCard(String lobbyCode, IPlayer player) throws PlayerManagementException;

    /**
     * Sets the starting position for a player in a specified city.
     *
     * @param lobbyCode the code of the lobby
     * @param cityName  the name of the city
     * @param player    the player whose starting position is being set
     * @throws PlayerManagementException if an error occurs while setting the starting position
     */
    void setStartingPosition(String lobbyCode, CityName cityName, IPlayer player) throws PlayerManagementException;

    /**
     * Adds a card to a player's hand in a specified lobby.
     *
     * @param lobbyId  the ID of the lobby
     * @param username the username of the player
     * @param card     the card to be added
     */
    void addCard(String lobbyId, String username, ICard card);

    /**
     * Discards a card for a player in a specified lobby.
     *
     * @param lobbyCode the code of the lobby
     * @param username  the username of the player
     * @param cardId    the ID of the card to be discarded
     * @throws GameException if an error occurs while discarding the card
     */
    void discardPlayerCard(String lobbyCode, String username, Integer cardId) throws GameException;

    /**
     * Discards multiple cards for a player in a specified lobby.
     *
     * @param lobbyCode the code of the lobby
     * @param username  the username of the player
     * @param cardIds   the list of IDs of the cards to be discarded
     * @throws GameException if an error occurs while discarding the cards
     */
    void discardPlayerCards(String lobbyCode, String username, List<Integer> cardIds) throws GameException;

    /**
     * Retrieves a card for a player in a specified lobby.
     *
     * @param lobbyId    the ID of the lobby
     * @param playerName the name of the player
     * @param cardId     the ID of the card
     * @return the card or null if the card does not exist
     * @throws PlayerManagementException if an error occurs while retrieving the card
     */
    ICard getCard(String lobbyId, String playerName, int cardId) throws PlayerManagementException;

    /**
     * Sets the location of a player to a specified city within a lobby.
     *
     * @param lobbyId    the ID of the lobby
     * @param playerName the name of the player
     * @param cityId     the id of the city where the player is to be located
     */
    void setPlayerLocation(String lobbyId, String playerName, int cityId);

    /**
     * Shuffles the infection cards in the discard pile and adds them to the draw pile.
     */
    void shuffleInfectionCardsFromDrawPile(IGame game);

    /**
     * Draws a player card from the DiscardPile. The specific behavior of this method should be defined.
     */
    InfectionCard drawBottomInfectionCard(IGame game) throws IllegalStateException;

    /**
     * Retrieves the cards that a player can sort.
     *
     * @param lobbyId the ID of the lobby
     * @param user    the user for whom the cards are to be sorted
     * @return the list of cards to be sorted
     * @throws GameException if an error occurs while retrieving the cards
     * @throws IllegalGameStateException     if the player is not in the correct state to sort cards
     */
    List<ICardDTO> getCardsToSort(String lobbyId, IUser user) throws GameException, IllegalGameStateException;

    /**
     * Retrieves the game with the specified lobby code.
     *
     * @param lobbyId the id of the lobby in which the game is happening
     * @return the game with the specified lobby code
     */
    IGame getGame(String lobbyId);

    /**
     * Retrieves the player with the specified user in the specified lobby.
     *
     * @param game the game in which the player is to be retrieved
     * @param user the user for whom the player is to be retrieved
     * @return the player with the specified user in the specified lobby
     */
    IPlayer getPlayerByUser(IUser user, IGame game);

    /**
     * Sorts the cards of the playerCardDrawPile in a lobby.
     *
     * @param lobbyId the ID of the lobby
     * @param user    the user sorting the cards
     * @param cards   the list of cards to be sorted
     * @throws IllegalStateException if the player is not in the correct state to sort cards
     */
    void sortCards(String lobbyId, IUser user, List<ICardDTO> cards) throws GameException;
}