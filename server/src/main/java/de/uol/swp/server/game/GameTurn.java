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

@AllArgsConstructor
@Getter
@Setter
public class GameTurn
{
    private static final Logger LOG = LogManager.getLogger(GameTurn.class);
    private Player currentPlayer;
    private Board board;
    private int actionsRemaining;
    private boolean isDrawPhase;
    private boolean isInfectionPhase;
    private boolean isTurnOver;

    public GameTurn(Player currentPlayer, Board board)
    {
        this.currentPlayer = currentPlayer;
        this.board = board;
        this.actionsRemaining = 4;
        this.isDrawPhase = false;
        this.isInfectionPhase = false;
        this.isTurnOver = false;
    }

    public void startTurn() throws InterruptedException
    {
        LOG.info("Starte Zug für " + currentPlayer.getUser()
                                                  .getUsername());
        while (!isTurnOver) {
            wait();
        }
    }

    public void processAction(Action action)
    {
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
            // Continue for other actions
            default:
                // Handle unknown action
                throw new IllegalArgumentException("Unknown action type: " + action.getActionType());
        }
        actionsRemaining--;
        checkTurnEnd();
    }

    private void checkTurnEnd()
    {
        if (actionsRemaining <= 0) {
            startDrawPhase();
        }
    }

    private void startDrawPhase()
    {
        isDrawPhase = true;
        drawPlayerCard();
        drawPlayerCard();

        startInfectionPhase();
    }

    private void startInfectionPhase()
    {
        isInfectionPhase = true;
        int infectionCounter = board.getInfectionCounter();
        for (int i = 1; i <= infectionCounter; i++) {
            infectCity(drawInfectionCard(), 1);
        }
        endTurn();
    }

    public void endTurn()
    {
        LOG.info("Turn ended for player: " + currentPlayer.getUser()
                                                          .getUsername());
        isTurnOver = true;
    }

    /**
     * Places water treatment markers in the specified region.
     *
     * @param region the region where water treatment markers will be placed
     * @param count  the number of water treatment markers to place
     *
     * @throws GameTurnException if there are not enough water treatment markers remaining
     */
    public void placeWaterTreatment(Region region, int count) throws GameTurnException
    {
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
     *
     * @throws GameTurnException if the city already has a hospital or if the player cannot build a hospital in the specified city
     */
    public void buildHospital(City city, boolean cityCardRequired) throws GameTurnException
    {
        List<City> cities = board.getCities();

        List<City> citiesWithHospital = cities.stream()
                                              .filter(City::isHasHospital)
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
                          .forEach(c -> c.setHasHospital(false));

        city.setHasHospital(true);
    }

    /**
     * Performs the action of building a hospital in the specified city.
     *
     * @param city the city where the hospital will be built
     *
     * @throws GameTurnException if the player does not have the required city card
     */
    private void buildHospitalAction(City city) throws GameTurnException
    {
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
     *
     * @throws GameTurnException if the train tracks cannot be built on the connection,
     *                           if the connection already has train tracks,
     *                           or if there are not enough tracks left to build
     */
    public void buildTrainTracks(Connection connection) throws GameTurnException
    {
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

    void tradeCards(Player tradingPartner)
    {
        //not implemented
    }

    void treatInfection(City city)
    {
        //not implemented
    }

    void researchPlague()
    {
        //not implemented
    }

    void useRoleAbility()
    {
        //not implemented
    }

    void move(City destination)
    {
        //not implemented
    }

    InfectionCard drawInfectionCard()
    {
        //not implemented
        return null;
    }

    void drawPlayerCard()
    {
        //not implemented
    }

    void infectCity(InfectionCard infectionCard, int amount)
    {
        //not implemented
    }


}

