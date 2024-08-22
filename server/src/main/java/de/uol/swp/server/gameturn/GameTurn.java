package de.uol.swp.server.gameturn;

import de.uol.swp.server.connection.Connection;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class GameTurn {

    private static int round;
    private static Player currentPlayer;
    private static Board board;
    @Setter
    private static int actionsRemaining;

    void placeWaterTreatment(Region region){
        //not implemented
    }
    void buildHospital(City city){
        //not implemented
    }
    void buildTrainTracks(Connection connection){
        //not implemented
    }
    void tradeCards(Player tradingPartner){
        //not implemented
    }
    void treatInfection(City city){
        //not implemented
    }
    void researchPlague(){
        //not implemented
    }
    void useRoleAbility(){
        //not implemented
    }
    void move(City destination){
        //not implemented
    }
    void drawInfectionCard(){
        //not implemented
    }
    void drawPlayerCard(){
        //not implemented
    }
    void infectCity(InfectionCard infectionCard){
        //not implemented
    }
    void infectCity(InfectionCard infectionCard, int amount){
        //not implemented
    }


}
