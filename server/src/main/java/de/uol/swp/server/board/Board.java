package de.uol.swp.server.board;

import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.region.RegionRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The Board class represents the game board, containing repositories for cities, regions, and connections,
 * as well as various game state information such as infection counters and card piles.
 */
@AllArgsConstructor
@Getter
public class Board {
    /**
     * Repository for managing city data.
     */
    private CityRepository cityRepository;

    /**
     * Repository for managing region data.
     */
    private RegionRepository regionRepository;

    /**
     * Repository for managing connection data.
     */
    private ConnectionRepository connectionRepository;

    /**
     * Counter for tracking the current infection level.
     */
    @Setter
    private int infectionCounter;

    /**
     * Counter for tracking the current escalation stage.
     */
    @Setter
    private int escalationStage;

    /**
     * List of infection cards in the draw pile.
     */
    private List<InfectionCard> infectionCardDrawPile;

    /**
     * List of infection cards in the discard pile.
     */
    private List<InfectionCard> infectionCardDiscardPile;

    /**
     * List of player cards in the draw pile.
     */
    private List<Card> playerCardDrawPile;

    /**
     * List of player cards in the discard pile.
     */
    private List<Card> playerCardDiscardPile;

    /**
     * Counter for tracking the number of water treatments left.
     */
    @Setter
    private int waterTreatmentsLeft;

    /**
     * Counter for tracking the number of tracks left.
     */
    @Setter
    private int tracksLeft;
}