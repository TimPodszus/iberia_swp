package de.uol.swp.server.game.data;

import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.game.GameStateChangeListener;
import de.uol.swp.server.game.states.IGameState;
import de.uol.swp.server.plague.data.PlagueRepository;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.region.RegionRepository;

import java.util.List;

/**
 * Interface representing a game with various repositories and game state methods.
 */
public interface IGame {

    /**
     * Gets the game ID.
     *
     * @return the game ID
     */
    String getGameId();

    /**
     * Gets the city repository.
     *
     * @return the city repository
     */
    CityRepository getCityRepository();

    /**
     * Gets the region repository.
     *
     * @return the region repository
     */
    RegionRepository getRegionRepository();

    /**
     * Gets the connection repository.
     *
     * @return the connection repository
     */
    ConnectionRepository getConnectionRepository();

    /**
     * Gets the plague repository.
     *
     * @return the plague repository
     */
    PlagueRepository getPlagueRepository();

    /**
     * Gets the infection counter.
     *
     * @return the infection counter
     */
    int getInfectionCounter();

    /**
     * Sets the infection counter.
     *
     * @param infectionCounter the new infection counter
     */
    void setInfectionCounter(int infectionCounter);

    /**
     * Gets the escalation state.
     *
     * @return the escalation state
     */
    int getEscalationStage();

    /**
     * Sets the escalation state.
     *
     * @param escalationStage the new escalation state
     */
    void setEscalationStage(int escalationStage);

    /**
     * Gets the number of water treatments left.
     *
     * @return the number of water treatments left
     */
    int getWaterTreatmentsLeft();

    /**
     * Sets the number of water treatments.
     *
     * @param waterTreatmentsLeft the new number of water treatments
     */
    void setWaterTreatmentsLeft(int waterTreatmentsLeft);

    /**
     * Gets the number of tracks left.
     *
     * @return the number of tracks left
     */
    int getTracksLeft();

    /**
     * Sets the number of tracks left.
     *
     * @param tracksLeft the new number of tracks left
     */
    void setTracksLeft(int tracksLeft);

    /**
     * Gets the infection card draw pile.
     *
     * @return the infection card draw pile
     */
    List<InfectionCard> getInfectionCardDrawPile();

    /**
     * Gets the infection card discard pile.
     *
     * @return the infection card discard pile
     */
    List<InfectionCard> getInfectionCardDiscardPile();

    /**
     * Gets the player card draw pile.
     *
     * @return the player card draw pile
     */
    List<ICard> getPlayerCardDrawPile();

    /**
     * Gets the player card discard pile.
     *
     * @return the player card discard pile
     */

    List<ICard> getPlayerCardDiscardPile();

    /**
     * Gets the list of players.
     *
     * @return the list of players
     */
    List<IPlayer> getPlayers();

    /**
     * Gets the current state of the game.
     *
     * @return the current state of the game
     */
    IGameState getState();

    /**
     * Sets the current state of the game.
     *
     * @param state the new state of the game
     */
    void setState(IGameState state);

    /**
     * Gets the previous state of the game.
     *
     * @return the previous state of the game
     */
    IGameState getPreviousState();

    /**
     * Sets the current player index.
     *
     * @param count the new current player index
     */
    void setCurrentPlayerIndex(int count);

    /**
     * Gets the current player index.
     *
     * @return the current player index
     */
    int getCurrentPlayerIndex();

    /**
     * Initializes the game with the specified difficulty.
     *
     * @param difficulty the difficulty level
     */
    void initializeGame(int difficulty);

    /**
     * Gets the difficulty level of the game.
     *
     * @return the difficulty level
     */
    int getDifficulty();

    /**
     * Gets the current player.
     *
     * @return the current player
     */
    IPlayer getCurrentPlayer();

    /**
     * Sets the GameStateChangeListener.
     *
     * @param listener the new GameStateChangeListener
     */
    void setGameStateChangeListener(GameStateChangeListener listener);

    /**
     * Gets the player by username.
     *
     * @param username the username of the player
     * @return the player with the specified username
     */
    IPlayer getPlayer(String username);

    /**
     * Sets the infection card draw pile.
     *
     * @param infectionCardDrawPile the new infection card draw pile
     */
    void setInfectionCardDrawPile(List<InfectionCard> infectionCardDrawPile);
}