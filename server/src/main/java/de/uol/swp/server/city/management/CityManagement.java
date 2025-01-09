package de.uol.swp.server.city.management;

import de.uol.swp.common.cards.CardType;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityName;
import de.uol.swp.server.game.GameException;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagement;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.infection.Infection;
import de.uol.swp.server.plague.Plague;
import de.uol.swp.server.plague.PlagueManagement;
import de.uol.swp.server.region.Region;

import java.util.ArrayList;
import java.util.List;

public class CityManagement implements ICityManagement {

    public void infectCityWithOneCube(IGame game, InfectionCard infectionCard, PlagueName plagueName) {
        infectCity(game, infectionCard, plagueName, 1);
    }

    public void infectCityWithOwnPlague(IGame game, InfectionCard infectionCard, int amount) {
        PlagueName plagueName = findCity(game, infectionCard).getPlagueName();
        infectCity(game, infectionCard, plagueName, amount);
    }

    public void infectCityWithOneCubeAndOwnPlague(IGame game, InfectionCard infectionCard) {
        PlagueName plagueName = findCity(game, infectionCard).getPlagueName();
        infectCity(game, infectionCard, plagueName, 1);
    }

    public void infectCity(
            IGame game,
            InfectionCard infectionCard,
            PlagueName plagueName,
            int amount
    ) throws CityManagementException {
        infectCity(game, infectionCard, plagueName, amount, true);
    }

    private void infectCity(
            IGame game,
            InfectionCard infectionCard,
            PlagueName plagueName,
            int amount,
            boolean triggerEscalation
    ) throws CityManagementException {
        validateParameters(game, infectionCard, plagueName, amount);

        City city = findCity(game, infectionCard);

        if (reduceWaterTreatments(game, city, amount) <= 0) {
            return;
        }

        Infection infection = findInfection(city, plagueName);
        Plague plague = game.getPlagueRepository()
                            .getPlagueByName(plagueName);

        increaseInfectionSeverity(game, infection, plague, amount, city, triggerEscalation);

        if (infectionCard.getId() != -1) {
            discardCard(game, infectionCard);
        }
    }

    private void validateParameters(
            IGame game,
            InfectionCard infectionCard,
            PlagueName plagueName,
            int amount
    ) throws CityManagementException {
        if (game == null || infectionCard == null || plagueName == null || amount < 0) {
            throw new CityManagementException("Invalid parameters");
        }
    }

    private City findCity(IGame game, InfectionCard infectionCard) throws CityManagementException {
        City city = game.getCityRepository()
                        .getCityByName(infectionCard.getCity()
                                                    .getName());
        if (city == null) {
            throw new CityManagementException("City not found");
        }
        return city;
    }

    private int reduceWaterTreatments(IGame game, City city, int amount) throws CityManagementException {
        List<Region> regions = game.getRegionRepository()
                                   .getRegionsByCityName(city.getName());
        int totalWaterTreatments = regions.stream()
                                          .mapToInt(Region::getWaterTreatments)
                                          .sum();

        try {
            if (totalWaterTreatments >= amount) {
                decreaseWaterTreatmentsInRegions(regions, amount);
                return 0;
            } else {
                decreaseWaterTreatmentsInAllRegions(regions);
                amount -= totalWaterTreatments;
                return amount;
            }
        } catch (GameException e) {
            throw new CityManagementException("Error while decreasing water treatments");
        }
    }

    private void decreaseWaterTreatmentsInRegions(List<Region> regions, int amount) throws GameException {
        for (Region region : regions) {
            int waterTreatments = region.getWaterTreatments();
            if (waterTreatments >= amount) {
                //TODO: Den Spieler die Regions wählen lassen
                region.decreaseWaterTreatments(amount);
                return;
            } else {
                //TODO: Den Spieler die Regions wählen lassen
                region.decreaseWaterTreatments(waterTreatments);
                amount -= waterTreatments;
            }
        }
    }

    private void decreaseWaterTreatmentsInAllRegions(List<Region> regions) throws GameException {
        for (Region region : regions) {
            region.decreaseWaterTreatments(region.getWaterTreatments());
        }
    }

    private Infection findInfection(City city, PlagueName plagueName) throws CityManagementException {
        return city.getInfections()
                   .stream()
                   .filter(i -> i.getPlague()
                                 .getName()
                                 .equals(plagueName))
                   .findFirst()
                   .orElseThrow(() -> new CityManagementException("Infection not found"));
    }

    private void discardCard(IGame game, InfectionCard infectionCard) {
        IGameManagement gameManagement = new GameManagement();
        gameManagement.discardInfectionCard(game, infectionCard);
    }

    public void escalation(IGame game, CityName cityName, List<CityName> escalatedCities) {
        List<CityName> connectedCityNames = game.getConnectionRepository()
                                                .getCityNamesOfConnectedCitiesByCityName(cityName);
        List<City> connectedCities = game.getCityRepository()
                                         .getCitiesByNames(connectedCityNames);

        List<CityName> newlyEscalatedCities = new ArrayList<>();
        for (City connectedCity : connectedCities) {
            if (escalatedCities.contains(connectedCity.getName())) {
                continue;
            }

            infectCity(
                    game, new InfectionCard(-1, "", CardType.INFECTION_CARD, connectedCity),
                    //TODO: mit der plague die den ausbruch ausgelöst hat
                    connectedCity.getPlagueName(), 1, false
            );

            if (hasExceededSeverity(connectedCity)) {
                newlyEscalatedCities.add(connectedCity.getName());
            }
        }

        escalatedCities.addAll(newlyEscalatedCities);

        for (CityName newlyEscalatedCity : newlyEscalatedCities) {
            //TODO: wenn es zwei newlyEscalated gibt und der eine mit seinem escalation-Durchlauf fertig ist, sind
            // die cityNames aus dem durchlauf der escalation weg, oder?
            escalation(game, newlyEscalatedCity, escalatedCities);
        }
    }

    private boolean hasExceededSeverity(City city) {
        return city.getInfections()
                   .stream()
                   .anyMatch(i -> i.getPlague()
                                   .getName()
                                   .equals(city.getPlagueName()) && i.getSeverity() > 3);
    }

    private void increaseInfectionSeverity(
            IGame game,
            Infection infection,
            Plague plague,
            int amount,
            City city,
            boolean triggerEscalation
    ) {
        int newSeverity = Math.min(infection.getSeverity() + amount, 3);
        int cubesUsed = newSeverity - infection.getSeverity();
        boolean wouldEscalate = infection.getSeverity() + amount > 3;

        infection.setSeverity(newSeverity);
        plague.setCubesRemaining(plague.getCubesRemaining() - cubesUsed);

        if (wouldEscalate && triggerEscalation) {
            escalation(game, city.getName(), List.of(city.getName()));
        }

        if (new PlagueManagement().isCubeCountNegative(game, plague.getName())) {
            //TODO: GameOver auslösen (Implementierung mit Issue #137)
        }
    }
}
