package de.uol.swp.server.game.management;

import de.uol.swp.common.game.GameActions;
import de.uol.swp.common.game.message.event.ShareKnowledgeEvent;
import de.uol.swp.common.game.message.request.CreateGameRequest;
import de.uol.swp.common.game.message.request.PositioningRequest;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.cards.management.CardNotFoundException;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.connection.data.IConnection;
import de.uol.swp.server.game.GameService;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.GameInitializationException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.exceptions.LobbyIsEmptyException;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.usermanagement.IUser;

import java.util.List;

public interface IGameManagement {

    /**
     * Creates and initializes a new game based on the provided request.
     *
     * @param request the game creation request containing user and difficulty information
     * @return the newly created game
     * @throws GameInitializationException if creating and initializing the game fails
     */
    IGame createAndInitializeGame(CreateGameRequest request) throws GameInitializationException;

    /**
     * Sets the initial positioning of a player in the game based on the provided city.
     *
     * @param request The request with where the position is to be set
     * @throws GameException             if setting the positioning fails
     * @throws IllegalGameStateException if the game is in a state that does not allow positioning
     */
    IGame setPositioning(PositioningRequest request) throws GameException, IllegalGameStateException;

    /**
     * Draws a player card. The specific behavior of this method should be defined.
     */
    InfectionCard drawInfectionCard(IGame game);

    /**
     * Adds an infection card to the infection card discard pile.
     */
    void discardInfectionCard(IGame game, InfectionCard infectionCard);

    /**
     * Retrieves the list of available actions for a given lobby.
     *
     * @param lobbyId the ID of the lobby for which to retrieve available actions
     * @return a list of available game actions
     */
    List<GameActions> getAvailableActions(String lobbyId, IUser user);

    /**
     * Moves a player to a specified city in the game.
     *
     * @param user    the user representing the player to be moved
     * @param lobbyId the id of the lobby in which the game is happening
     * @param city    the city to which the player will be moved
     * @param card    the card used to move the player
     *                <p>
     * @throws GameException             if moving the player fails
     * @throws IllegalGameStateException if the game is in an illegal state
     */
    void movePlayer(IUser user, String lobbyId, ICity city, ICard card) throws GameException, IllegalGameStateException;

    /**
     * Retrieves the game with the specified lobby code.
     *
     * @param lobbyId the id of the lobby in which the game is happening
     * @return the game with the specified lobby code
     */
    IGame getGame(String lobbyId);

    /**
     * Builds a train track between two cities in the game.
     *
     * @param user       the user representing the player building the train track
     * @param lobbyId    the id of the lobby in which the game is happening
     * @param connection the connection representing the train track to be built
     * @throws GameException             if building the train track fails
     * @throws IllegalGameStateException if the game is in a state, where building a train track is not allowed
     */
    void buildTrainTrack(
            IUser user,
            String lobbyId,
            IConnection connection
    ) throws GameException, IllegalGameStateException;

    /**
     * Locks the game in a wait-for-confirmation state.
     *
     * @param lobbyId the ID of the lobby in which the game is happening
     */
    void lockGameInWaitForConfirmation(String lobbyId);

    /**
     * Unlocks the game from a wait-for-confirmation state and sets the game state to the previous state.
     *
     * @param lobbyId the ID of the lobby in which the game is happening
     */
    void unlockGameInWaitForConfirmation(String lobbyId);

    /**
     * Handles the acceptance of a share knowledge request.
     *
     * @param currentPlayer the player currently taking the action
     * @param targetPlayer  the player with whom knowledge is being shared
     * @param lobbyId       the ID of the lobby in which the game is happening
     * @param event         the event representing the share knowledge request
     * @param gameService   the game service
     * @throws PlayerManagementException if an error occurs during the process
     */
    void shareKnowledgeRequestAccepted(
            IPlayer currentPlayer,
            IPlayer targetPlayer,
            String lobbyId,
            ShareKnowledgeEvent event,
            GameService gameService
    ) throws PlayerManagementException;


    /**
     * Increases the number of actions the current player has in the game.
     *
     * @param game   the game in which the current player's actions are to be increased
     * @param amount the amount by which to increase the current player's actions
     */
    void increaseCurrentPlayerActions(IGame game, int amount);

    /**
     * Shares knowledge by discarding a card and receiving another card.
     *
     * @param cardToDiscardID the ID of the card to be discarded
     * @param cardToReceiveID the ID of the card to be received
     * @param lobbyId         the ID of the lobby in which the game is happening
     * @param service         the game service
     * @throws CardNotFoundException     if the card to be discarded or received is not found
     * @throws PlayerManagementException if an error occurs during the process
     */
    void shareKnowledgeWithDiscardPile(
            int cardToDiscardID,
            int cardToReceiveID,
            String lobbyId,
            GameService service
    ) throws CardNotFoundException, PlayerManagementException;

    /**
     * Ends the turn for the current player in the specified lobby.
     *
     * @param lobbyId the ID of the lobby in which the game is happening
     * @param user    the user representing the player whose turn is to be ended
     * @throws IllegalGameStateException if the turn cannot be ended due to the current game state
     */
    void endTurn(String lobbyId, IUser user) throws IllegalGameStateException;

    void removePlayer(String lobbyCode, IUser user) throws LobbyIsEmptyException;

    void removeGame(String lobbyCode);
}

