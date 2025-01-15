package de.uol.swp.server.game.data;

import de.uol.swp.common.cards.CardType;
import de.uol.swp.server.cards.Card;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.cards.EpidemicCard;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.city.management.CityManagement;
import de.uol.swp.server.connection.ConnectionRepository;
import de.uol.swp.server.game.management.GameManagement;
import de.uol.swp.server.game.states.IGameState;
import de.uol.swp.server.game.states.StartState;
import de.uol.swp.server.plague.data.PlagueRepository;
import de.uol.swp.server.player.data.Player;
import de.uol.swp.server.player.management.PlayerManagement;
import de.uol.swp.server.region.RegionRepository;
import de.uol.swp.server.role.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public class Game implements IGame {
    /**
     * Unique identifier for the game.
     */
    private String gameId;

    /**
     * Repository for role-related data.
     */
    private RoleRepository roleRepository;

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
     * Repository for the plagues.
     */
    private PlagueRepository plagueRepository;

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
     * List of players in the game.
     */
    private List<Player> players;

    /**
     * Index of the current player.
     */
    @Setter
    private int currentPlayerIndex;

    /**
     * Current state of the game.
     */
    @Setter
    private IGameState state;

    /**
     * Previous state of the game.
     */
    @Setter
    private IGameState previousState;

    /**
     * Management class for game-related operations.
     */
    private GameManagement gameManagement;

    /**
     * Management class for player-related operations.
     */
    private PlayerManagement playerManagement;

    /**
     * Management class for city-related operations.
     */
    private CityManagement cityManagement;

    /**
     * Difficulty level of the game.
     */
    private int difficulty;

    /**
     * Constructs a new Game instance with default values.
     * Initializes repositories and sets initial game state.
     */
    public Game(int difficulty, String lobbyCode) {
        this.gameId = lobbyCode;
        this.cityRepository = new CityRepository();
        this.regionRepository = new RegionRepository(this.cityRepository);
        this.connectionRepository = new ConnectionRepository();
        this.plagueRepository = new PlagueRepository();
        this.infectionCounter = 1;
        this.escalationStage = 0;
        this.waterTreatmentsLeft = 14;
        this.tracksLeft = 20;
        this.difficulty = difficulty;
        this.infectionCardDrawPile = new ArrayList<>();
        this.infectionCardDiscardPile = new ArrayList<>();
        this.playerCardDrawPile = new ArrayList<>();
        this.playerCardDiscardPile = new ArrayList<>();
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.gameManagement = new GameManagement();
        this.playerManagement = new PlayerManagement(this);
        this.cityManagement = new CityManagement();
        this.state = new StartState();
        state.handleAction(this, null);
    }

    public void initializeGame(int difficulty) {
        createInfectionCards(cityRepository.getCities());
        createPlayerCards(cityRepository.getCities());
        Collections.shuffle(getInfectionCardDrawPile());
        gameStartShuffle(difficulty);
    }

    public void createInfectionCards(List<ICity> cities) {
        int i = 0;
        for (ICity city : cities) {
            InfectionCard infectionCard = new InfectionCard(
                    i,
                    city.getName()
                        .toString(),
                    CardType.INFECTION_CARD,
                    city
            );
            infectionCardDrawPile.add(infectionCard);
            i++;
        }
    }

    public void createPlayerCards(List<ICity> cities) {
        int i = 1;
        for (ICity city : cities) {
            CityCard citycard = new CityCard(
                    i,
                    city.getName()
                        .toString(),
                    CardType.CITY_CARD,
                    city
            );
            playerCardDrawPile.add(citycard);
            i++;
        }
    }

    public EpidemicCard createEpidemicCard(int id) {
        return new EpidemicCard(id, "Epidemiekarte", CardType.EPIDEMIC_CARD, "");
    }

    public void gameStartShuffle(int numSubDecks) {
        if (numSubDecks <= 0) {
            throw new IllegalArgumentException("Number of sub-decks must be greater than zero.");
        }
        List<List<Card>> subDecks = splitIntoSubDecks(playerCardDrawPile, numSubDecks);
        for (int i = 0; i < numSubDecks; i++) {
            subDecks.get(i)
                    .add(createEpidemicCard(numSubDecks));
            Collections.shuffle(subDecks.get(i));
        }
        playerCardDrawPile.clear();
        for (List<Card> deck : subDecks) {
            playerCardDrawPile.addAll(deck);
        }
    }

    private List<List<Card>> splitIntoSubDecks(List<Card> deck, int numSubDecks) {
        List<List<Card>> subDecks = new ArrayList<>();
        int subDeckSize = deck.size() / numSubDecks;
        int leftover = deck.size() % numSubDecks;

        for (int i = 0; i < numSubDecks; i++) {
            int start = i * subDeckSize + Math.min(i, leftover);
            int end = start + subDeckSize + (i < leftover ? 1 : 0);
            subDecks.add(new ArrayList<>(deck.subList(start, end)));
        }

        return subDecks;
    }

    /**
     * Retrieves the current player whose turn it is in the game.
     *
     * @return the {@link Player} object representing the current player
     */
    public Player getCurrentPlayer() {
        return this.players.get(currentPlayerIndex);
    }
}
