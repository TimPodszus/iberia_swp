package de.uol.swp.server.gameturn;

import de.uol.swp.server.board.Board;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.connection.Connection;
import de.uol.swp.server.player.Player;
import de.uol.swp.server.region.Region;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class GameTurn
{
    private final int round;
    private final Player currentPlayer;
    private final Board board;
    @Setter
    private int actionsRemaining;

    void placeWaterTreatment(Region region)
    {
        //not implemented
    }

    void buildHospital(City city)
    {
        //not implemented
    }

    void buildTrainTracks(Connection connection) throws Exception
    {
        if (!connection.isCanBuildTrainTracks()) {
            throw new Exception("Auf dieser Verbindung kann keine Zugstrecke gebaut werden");
        }

        if (connection.isHasTrainTrack()) {
            throw new Exception("Auf dieser Verbindung existiert bereits eine Zugstrecke");
        }

        if (board.getTracksLeft() < 1) {
            throw new Exception("Es sind nichtmehr genug Schienen vorhanden!");
        }

        connection.setHasTrainTrack(true);
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

    void infectCity(InfectionCard infectionCard)
    {
        //not implemented
    }

    void infectCity(InfectionCard infectionCard, int amount)
    {
        //not implemented
    }


}
