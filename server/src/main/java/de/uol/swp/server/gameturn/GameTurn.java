package de.uol.swp.server.gameturn;

import de.uol.swp.server.board.Board;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.player.Player;
import de.uol.swp.server.region.Region;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Getter
public class GameTurn
{
    private final int round;
    private final Player currentPlayer;
    private final Board board;
    @Setter
    private int actionsRemaining;

    void placeWaterTreatment(Region region, int count) throws GameTurnException
    {
        int waterTreatmentsRemaining = board.getWaterTreatmentsLeft();

        if (waterTreatmentsRemaining < count) {
            throw new GameTurnException("Es sind nichtmehr genug Wasseraufbereitungsmarker vorhanden!");
        }

        region.increaseWaterTreatments(count);
        board.setWaterTreatmentsLeft(waterTreatmentsRemaining - count);
    }

    void buildHospital(City city, boolean cityCardRequired) throws GameTurnException
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
        if (cityCardRequired && currentPlayer.getCity() == city) {
            buildHospitalAction(city);
        }
        // Für die Ereigniskarte "Krankenhausgründung"
        else if (currentPlayer.getCity()
                              .getPlagueName() != city.getPlagueName()) {
            throw new GameTurnException("Der Spieler kann nur auf einer gleichfarbigen Stadt ein Krankenhaus platzieren");
        }

        citiesWithHospital.stream()
                          .filter(c -> c.getPlagueName()
                                        .equals(city.getPlagueName()))
                          .forEach(c -> c.setHasHospital(false));

        city.setHasHospital(true);
    }

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
