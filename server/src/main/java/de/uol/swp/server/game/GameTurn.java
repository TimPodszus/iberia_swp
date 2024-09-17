package de.uol.swp.server.game;

import de.uol.swp.common.enums.Action;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.connection.Connection;
import de.uol.swp.server.player.Player;
import de.uol.swp.server.region.Region;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Getter
public class GameTurn {
    private static final Logger LOG = LogManager.getLogger(GameTurn.class);
    private Player currentPlayer;
    private Board board;
    private int actionsRemaining;
    private boolean isDrawPhase;
    private boolean isInfectionPhase;
    private boolean isTurnOver;

    public GameTurn(Player currentPlayer, Board board) {
        this.currentPlayer = currentPlayer;
        this.board = board;
        this.actionsRemaining = 4;
        this.isDrawPhase = false;
        this.isInfectionPhase = false;
        this.isTurnOver = false;
    }

    public void startTurn() throws InterruptedException {
        LOG.info("Starte Zug für " + currentPlayer.getUser()
                                                            .getUsername());
        while (!isTurnOver) {
            wait();
        }
    }

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
            // Continue for other actions
            default:
                // Handle unknown action
                throw new IllegalArgumentException("Unknown action type: " + action.getActionType());
        }
        actionsRemaining--;
        checkTurnEnd();
    }

    private void checkTurnEnd() {
        if (actionsRemaining <= 0) {
            startDrawPhase();
        }
    }

    private void startDrawPhase() {
        isDrawPhase = true;
        drawPlayerCard();
        drawPlayerCard();

        startInfectionPhase();
    }

    private void startInfectionPhase() {
        isInfectionPhase = true;
        int infectionCounter = board.getInfectionCounter();
        for (int i = 1; i <= infectionCounter; i++) {
            infectCity(drawInfectionCard(), 1);
        }
        endTurn();
    }

    public void endTurn() {
        LOG.info("Turn ended for player: " + currentPlayer.getUser()
                                                                    .getUsername());
        isTurnOver = true;
    }

    void placeWaterTreatment(Region region) {
        //not implemented
    }

    public void buildHospital(City city, boolean cityCardRequired) throws Exception {
        List<City> cities = board.getCities();

        List<City> citiesWithHospital = cities.stream()
                                              .filter(City::isHasHospital)
                                              .toList();

        boolean cityAlreadyHasHospital = citiesWithHospital.stream()
                                                           .anyMatch(c -> c.equals(city));

        if (cityAlreadyHasHospital) {
            throw new Exception("Die ausgewählte Stadt besitzt bereits ein Krankenhaus!");
        }

        // Für die Aktion "Krankenhaus bauen"
        if (cityCardRequired && currentPlayer.getCity() == city) {
            buildHospitalAction(city);
        }
        // Für die Ereigniskarte "Krankenhausgründung"
        else if (currentPlayer.getCity()
                              .getPlagueName() != city.getPlagueName()) {
            throw new Exception("Der Spieler kann nur auf einer gleichfarbigen Stadt ein Krankenhaus platzieren");
        }

        citiesWithHospital.stream()
                          .filter(c -> c.getPlagueName()
                                        .equals(city.getPlagueName()))
                          .forEach(c -> c.setHasHospital(false));

        city.setHasHospital(true);
    }

    public void buildHospitalAction(City city) throws Exception {
        Optional<CityCard> card = currentPlayer.getCards()
                                               .stream()
                                               .filter(CityCard.class::isInstance)
                                               .map(CityCard.class::cast)
                                               .filter(cityCard -> cityCard.getCity()
                                                                           .equals(city))
                                               .findFirst();

        if (card.isEmpty()) {
            throw new Exception(
                    "Der Spieler muss auf der ausgewählten Stadt stehen und die zugehörige Stadtkarte besitzen");
        }

        currentPlayer.discardCard(card.get());
    }

    void buildTrainTracks(Connection connection) {
        //not implemented
    }

    void tradeCards(Player tradingPartner) {
        //not implemented
    }

    void treatInfection(City city) {
        //not implemented
    }

    void researchPlague() {
        //not implemented
    }

    void useRoleAbility() {
        //not implemented
    }

    void move(City destination) {
        //not implemented
    }

    InfectionCard drawInfectionCard() {
        //not implemented
        return null;
    }

    void drawPlayerCard() {
        //not implemented
    }

    void infectCity(InfectionCard infectionCard, int amount) {
        //not implemented
    }


}

