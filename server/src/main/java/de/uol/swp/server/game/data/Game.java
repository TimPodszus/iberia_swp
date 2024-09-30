package de.uol.swp.server.game.data;

import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.region.RegionRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class Game implements IGame {
    /**
     * Unique identifier for the game.
     */
    private String gameId;

    /**
     * Repository for city-related data.
     */
    private CityRepository cityRepository;

    /**
     * Repository for region-related data.
     */
    private RegionRepository regionRepository;

    /**
     * Repository for connection-related data.
     */
    private ConnectionRepository connectionRepository;

    /**
     * Counter for the number of infections.
     */
    @Setter
    private int infectionCounter;

    /**
     * Current escalation stage of the game.
     */
    @Setter
    private int escalationStage;

    /**
     * Number of water treatments left.
     */
    @Setter
    private int waterTreatmentsLeft;

    /**
     * Number of tracks left.
     */
    @Setter
    private int tracksLeft;

    /**
     * Draw pile for infection cards.
     */
    private List<InfectionCard> infectionCardDrawPile;

    /**
     * Discard pile for infection cards.
     */
    private List<InfectionCard> infectionCardDiscardPile;

    /**
     * Draw pile for player cards.
     */
    private List<Card> playerCardDrawPile;

    /**
     * Discard pile for player cards.
     */
    private List<Card> playerCardDiscardPile;

    /**
     * Constructs a new Game instance with default values.
     * Initializes repositories and sets initial game state.
     */
    public Game() {
        this.cityRepository = new CityRepository();
        this.regionRepository = new RegionRepository(this.cityRepository);
        this.connectionRepository = new ConnectionRepository();
        this.infectionCounter = 0;
        this.escalationStage = 0;
        this.waterTreatmentsLeft = 0;
        this.tracksLeft = 0;
        this.infectionCardDrawPile = new ArrayList<>();
        this.infectionCardDiscardPile = new ArrayList<>();
        this.playerCardDrawPile = new ArrayList<>();
        this.playerCardDiscardPile = new ArrayList<>();
    }
}
