package de.uol.swp.server.game.data;

import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.game.states.IGameState;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.plague.PlagueRepository;
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
    List<Card> getPlayerCardDrawPile();

    /**
     * Gets the player card discard pile.
     *
     * @return the player card discard pile
     */

    List<Card> getPlayerCardDiscardPile();

    List<Player> getPlayers();

    ICityManagement getCityManagement();

    Object getState();

    void setState(IGameState state);

    IGameManagement getGameManagement();

    IGameState getPreviousState();

    void setCurrentPlayerIndex(int count);

    int getCurrentPlayerIndex();

    void initializeGame(int difficulty);

    int getDifficulty();

    Player getCurrentPlayer();
}