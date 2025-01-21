package de.uol.swp.common.game.dto;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.cards.InfectionCardDTO;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.connection.IConnectionDTO;
import de.uol.swp.common.plague.IPlagueDTO;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.common.region.IRegionDTO;

import java.util.List;

/**
 * Interface representing the Data Transfer Object (DTO) for a game.
 */
public interface IGameDTO {

    /**
     * Gets the unique identifier of the game.
     *
     * @return the game ID
     */
    String getGameId();

    /**
     * Gets the list of cities in the game.
     *
     * @return the list of cities
     */
    List<ICityDTO> getCities();

    /**
     * Gets the list of connections between cities in the game.
     *
     * @return the list of connections
     */
    List<IConnectionDTO> getConnections();

    /**
     * Gets the list of regions in the game.
     *
     * @return the list of regions
     */
    List<IRegionDTO> getRegions();

    /**
     * Gets the list of plagues in the game.
     *
     * @return the list of plagues
     */
    List<IPlagueDTO> getPlagues();

    /**
     * Gets the draw pile of infection cards.
     *
     * @return the draw pile of infection cards
     */
    List<InfectionCardDTO> getInfectionCardDrawPile();

    /**
     * Gets the discard pile of infection cards.
     *
     * @return the discard pile of infection cards
     */
    List<InfectionCardDTO> getInfectionCardDiscardPile();

    /**
     * Gets the draw pile of player cards.
     *
     * @return the draw pile of player cards
     */
    List<ICardDTO> getPlayerCardDrawPile();

    /**
     * Gets the discard pile of player cards.
     *
     * @return the discard pile of player cards
     */
    List<ICardDTO> getPlayerCardDiscardPile();

    /**
     * Gets the list of players in the game.
     *
     * @return the list of players
     */
    List<IPlayerDTO> getPlayers();

    /**
     * Gets the current infection counter.
     *
     * @return the infection counter
     */
    int getInfectionCounter();

    /**
     * Gets the current escalation stage.
     *
     * @return the escalation stage
     */
    int getEscalationStage();

    /**
     * Gets the number of water treatments left.
     *
     * @return the number of water treatments left
     */
    int getWaterTreatmentsLeft();

    /**
     * Gets the number of tracks left.
     *
     * @return the number of tracks left
     */
    int getTracksLeft();

    /**
     * Gets the index of the current player.
     *
     * @return the index of the current player
     */
    int getCurrentPlayerIndex();

    String getState();

    /**
     * Gets the current player.
     *
     * @return the current player
     */
    IPlayerDTO getCurrentPlayer();
}