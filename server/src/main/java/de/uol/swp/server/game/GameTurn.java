package de.uol.swp.server.game;

import de.uol.swp.common.enums.Action;
import de.uol.swp.server.board.Board;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.connection.Connection;
import de.uol.swp.server.player.Player;
import de.uol.swp.server.region.Region;
import lombok.*;

@AllArgsConstructor
@Getter
public class GameTurn {
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
        System.out.println("Starte Zug für " + currentPlayer.getUser()
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
        System.out.println("Turn ended for player: " + currentPlayer.getUser()
                                                                    .getUsername());
        isTurnOver = true;
    }

    void placeWaterTreatment(Region region) {
        //not implemented
    }

    void buildHospital(City city) {
        //not implemented
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

    private InfectionCard drawInfectionCard() {
        return null;
    }

    void drawPlayerCard() {
        //not implemented
    }

    void infectCity(InfectionCard infectionCard, int amount) {
        //not implemented
    }


}
