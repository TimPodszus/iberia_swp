package de.uol.swp.server.game;

import de.uol.swp.common.game.Action;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.connection.Connection;
import de.uol.swp.server.player.Player;
import de.uol.swp.server.region.Region;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Represents a game turn, managing the current player, board state, and phases of the turn.
 */
@AllArgsConstructor
@Getter
@Setter
public class GameTurn {
    private static final Logger LOG = LogManager.getLogger(GameTurn.class);

    /**
     * The player whose turn it is.
     */
    private Player currentPlayer;

    /**
     * The game board.
     */
    private Board board;

    /**
     * The number of actions remaining for the current player.
     */
    private int actionsRemaining;

    /**
     * Indicates if it is the draw phase.
     */
    private boolean isDrawPhase;

    /**
     * Indicates if it is the infection phase.
     */
    private boolean isInfectionPhase;

    /**
     * Indicates if the turn is over.
     */
    private boolean isTurnOver;

    /**
     * Constructor to initialize a new game turn with the specified player and board.
     *
     * @param currentPlayer the player whose turn it is
     * @param board         the game board
     */
    public GameTurn(Player currentPlayer, Board board) {
        this.currentPlayer = currentPlayer;
        this.board = board;
        this.actionsRemaining = 4;
        this.isDrawPhase = false;
        this.isInfectionPhase = false;
        this.isTurnOver = false;
    }

    /**
     * Starts the turn for the current player.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public void startTurn() throws InterruptedException {
        LOG.info(
                "Starte Zug für {}",
                currentPlayer.getUser()
                             .getUsername()
        );
        while (!isTurnOver) {
            wait();
        }
    }

    /**
     * Processes the specified action for the current player.
     *
     * @param action the action to process
     */
    public void processAction(Action action) {
        switch (action.getActionType()) {
            case MOVE:
                // Handle movement logic
                break;
            case BUILD_HOSPITAL:
                // Handle building a hospital
                break;
            case TRADE_CARDS:
                // Handle trading cards
                break;
            case TREAT_INFECTION:
                // Handle treating an infection
                break;
            case USE_ROLE_ABILITY:
                // Handle role ability
                break;
            case RESEARCH_PLAGUE:
                // Handle researching a plague
                break;
            case PLACE_WATER_TREATMENT:
                // Handle placing water treatments
                break;
            case BUILD_TRAIN_TRACKS:
                // Handle building train tracks
                break;
            default:
                // Handle unknown action
                throw new IllegalArgumentException("Unknown action type: " + action.getActionType());
        }
        actionsRemaining--;
        checkTurnEnd();
    }

    /**
     * Checks if the turn should end and starts the draw phase if no actions remain.
     */
    private void checkTurnEnd() {
        if (actionsRemaining <= 0) {
            startDrawPhase();
        }
    }

    /**
     * Starts the draw phase, drawing player cards and then starting the infection phase.
     */
    private void startDrawPhase() {
        isDrawPhase = true;
        drawPlayerCard();
        drawPlayerCard();
        startInfectionPhase();
    }

    /**
     * Starts the infection phase, infecting cities based on the infection counter.
     */
    private void startInfectionPhase() {
        isInfectionPhase = true;
        int infectionCounter = board.getInfectionCounter();
        for (int i = 1; i <= infectionCounter; i++) {
            infectCity(drawInfectionCard(), 1);
        }
        endTurn();
    }

    /**
     * Ends the current turn.
     */
    public void endTurn() {
        LOG.info("Turn ended for player: {}",
                currentPlayer.getUser()
                             .getUsername()
        );
        isTurnOver = true;
    }

    /**
     * Places water treatment markers in the specified region.
     *
     * @param region the region where water treatment markers will be placed
     * @param count  the number of water treatment markers to place
     * @throws GameTurnException if there are not enough water treatment markers remaining
     */
    public void placeWaterTreatment(Region region, int count) throws GameTurnException {
        int waterTreatmentsRemaining = board.getWaterTreatmentsLeft();

        if (waterTreatmentsRemaining < count) {
            throw new GameTurnException("Es sind nicht mehr genug Wasseraufbereitungsmarker vorhanden!");
        }

        region.increaseWaterTreatments(count);
        board.setWaterTreatmentsLeft(waterTreatmentsRemaining - count);
    }

    /**
     * Builds a hospital in the specified city.
     *
     * @param city             the city where the hospital will be built
     * @param cityCardRequired whether a city card is required to build the hospital
     * @throws GameTurnException if the city already has a hospital or if the player cannot build a hospital in the specified city
     */
    public void buildHospital(City city, boolean cityCardRequired) throws GameTurnException {
        List<City> cities = board.getCityRepository()
                                 .getCities();

        List<City> citiesWithHospital = cities.stream()
                                              .filter(City::isHospitalBuilt)
                                              .toList();

        boolean cityAlreadyHasHospital = citiesWithHospital.stream()
                                                           .anyMatch(c -> c.equals(city));

        if (cityAlreadyHasHospital) {
            throw new GameTurnException("Die ausgewählte Stadt besitzt bereits ein Krankenhaus!");
        }

        // Für die Aktion "Krankenhaus bauen"
        if (cityCardRequired && currentPlayer.getCurrentPosition() == city) {
            buildHospitalAction(city);
        }
        // Für die Ereigniskarte "Krankenhausgründung"
        else if (currentPlayer.getCurrentPosition()
                              .getPlagueName() != city.getPlagueName()) {
            throw new GameTurnException("Der Spieler kann nur auf einer gleichfarbigen Stadt ein Krankenhaus platzieren");
        }

        citiesWithHospital.stream()
                          .filter(c -> c.getPlagueName()
                                        .equals(city.getPlagueName()))
                          .forEach(c -> c.setHospitalBuilt(false));

        city.setHospitalBuilt(true);
    }

    /**
     * Performs the action of building a hospital in the specified city.
     *
     * @param city the city where the hospital will be built
     * @throws GameTurnException if the player does not have the required city card
     */
    private void buildHospitalAction(City city) throws GameTurnException {
        Optional<CityCard> card = currentPlayer.getCards()
                                               .stream()
                                               .filter(CityCard.class::isInstance)
                                               .map(CityCard.class::cast)
                                               .filter(cityCard -> cityCard.getCity()
                                                                           .equals(city))
                                               .findFirst();

        if (card.isEmpty()) {
            throw new GameTurnException(
                    "Der Spieler muss auf der ausgewählten Stadt stehen und die zugehörige Stadtkarte besitzen");
        }

        currentPlayer.discardCard(card.get());
    }

    /**
     * Builds train tracks on the specified connection.
     *
     * @param connection the connection where the train tracks will be built
     * @throws GameTurnException if the train tracks cannot be built on the connection,
     *                           if the connection already has train tracks,
     *                           or if there are not enough tracks left to build
     */
    public void buildTrainTracks(Connection connection) throws GameTurnException {
        if (!connection.isTrainTrackBuildable()) {
            throw new GameTurnException("Auf dieser Verbindung kann keine Zugstrecke gebaut werden");
        }

        if (connection.isTrainTrack()) {
            throw new GameTurnException("Auf dieser Verbindung existiert bereits eine Zugstrecke");
        }

        if (board.getTracksLeft() < 1) {
            throw new GameTurnException("Es sind nichtmehr genug Schienen vorhanden!");
        }

        connection.setTrainTrack(true);
        board.setTracksLeft(board.getTracksLeft() - 1);
    }

    /**
     * Trades cards with the specified trading partner.
     *
     * @param tradingPartner the player to trade cards with
     */
    void tradeCards(Player tradingPartner) {
        // not implemented
    }

    /**
     * Treats infection in the specified city.
     *
     * @param city the city where the infection will be treated
     */
    void treatInfection(City city) {
        // not implemented
    }

    /**
     * Researches a plague.
     */
    void researchPlague() {
        // not implemented
    }

    /**
     * Uses the role ability of the current player.
     */
    void useRoleAbility() {
        // not implemented
    }

    /**
     * Moves the current player to the specified destination city.
     *
     * @param destination the city to move to
     */
    void move(City destination) {
        // not implemented
    }

    /**
     * Draws an infection card.
     *
     * @return the drawn infection card
     */
    InfectionCard drawInfectionCard() {
        // not implemented
        return null;
    }

    /**
     * Draws a player card.
     */
    void drawPlayerCard() {
        // not implemented
    }

    /**
     * Infects the specified city with the given amount of infection.
     *
     * @param infectionCard the infection card representing the city to infect
     * @param amount        the amount of infection to add
     */
    void infectCity(InfectionCard infectionCard, int amount) {
        // not implemented
    }
}