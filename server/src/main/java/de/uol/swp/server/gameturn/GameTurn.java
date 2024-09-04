package de.uol.swp.server.gameturn;

import de.uol.swp.server.board.Board;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.connection.Connection;
import de.uol.swp.server.player.Player;
import de.uol.swp.server.region.Region;
import lombok.*;

@AllArgsConstructor
@Getter
public class GameTurn
{
    private Player currentPlayer;
    private Board board;
    private int actionsRemaining;
    private boolean isDrawPhase;
    private boolean isInfectionPhase;

    public GameTurn(Player currentPlayer, Board board)
    {
        this.currentPlayer = currentPlayer;
        this.board = board;
        this.actionsRemaining = 4;
        this.isDrawPhase = false;
        this.isInfectionPhase = false;
    }

    public void startTurn() {
        System.out.println("Starte Zug für " + currentPlayer.getUsername());
        executeActionPhase();
    }

    private void executeActionPhase() {
        while (actionsRemaining > 0) {
            // Hier sollte die Logik stehen, um eine Aktion auszuwählen und auszuführen.
            // Jede ausgeführte Aktion verringert actionsRemaining um 1.
        }

        startDrawPhase();
    }

    private void startDrawPhase() {
        isDrawPhase = true;
        drawPlayerCard();
        drawPlayerCard();

        startInfectionPhase();
    }

    private void startInfectionPhase() {
        isInfectionPhase = true;
        drawInfectionCard();
        // Logik zur Infektion von Städten entsprechend der gezogenen Infektionskarten

        endTurn();
    }
    private void endTurn() {
        System.out.println("Turn ended for player: " + currentPlayer.getUsername());
    }

    void placeWaterTreatment(Region region)
    {
        //not implemented
    }

    void buildHospital(City city)
    {
        //not implemented
    }

    void buildTrainTracks(Connection connection)
    {
        //not implemented
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

    void drawInfectionCard()
    {
        //not implemented
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
