package de.uol.swp.server.city.management;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.data.CityName;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagement;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.infection.management.IInfectionManagement;
import de.uol.swp.server.infection.management.InfectionManagement;
import de.uol.swp.server.plague.data.IPlague;
import de.uol.swp.server.plague.management.PlagueManagement;
import de.uol.swp.server.region.management.IRegionManagement;
import de.uol.swp.server.region.management.RegionManagement;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

/**
 * Manages city-related operations such as infecting cities with plagues,
 * handling escalations, and managing infection severity.
 */
public class CityManagement implements ICityManagement {
    private final IRegionManagement regionManagement = new RegionManagement();
    private final IGameManagement gameManagement = new GameManagement();
    private final IInfectionManagement infectionManagement = new InfectionManagement();

    /**
     * Infects a city with its own plague by a specified amount.
     *
     * @param game          the game instance
     * @param infectionCard the infection card representing the city to infect
     * @param amount        the amount of infection cubes to add
     */
    public void infectCityWithOwnPlague(IGame game, InfectionCard infectionCard, int amount) {
        PlagueName plagueName = findCity(game, infectionCard).getPlagueName();
        infectCity(game, infectionCard, plagueName, amount);
    }

    /**
     * Infects a city with a specified amount of a specified plague.
     *
     * @param game          the game instance
     * @param infectionCard the infection card representing the city to infect
     * @param plagueName    the name of the plague to infect the city with
     * @param amount        the amount of infection cubes to add
     * @throws CityManagementException if any parameter is invalid or an error occurs during infection
     */
    public void infectCity(
            IGame game,
            InfectionCard infectionCard,
            PlagueName plagueName,
            int amount
    ) throws CityManagementException {
        infectCity(game, findCity(game, infectionCard), plagueName, amount, true);
        gameManagement.discardInfectionCard(game, infectionCard);
    }

    /**
     * Infects a city with a specified amount of a specified plague, with an option to trigger escalation.
     *
     * @param game              the game instance
     * @param city              the city to infect
     * @param plagueName        the name of the plague to infect the city with
     * @param amount            the amount of infection cubes to add
     * @param triggerEscalation whether to trigger escalation if the infection severity exceeds the threshold
     * @throws CityManagementException if any parameter is invalid or an error occurs during infection
     */
    private void infectCity(
            IGame game,
            ICity city,
            PlagueName plagueName,
            int amount,
            boolean triggerEscalation
    ) throws CityManagementException {
        validateParameters(game, city, plagueName, amount);

        if (regionManagement.reduceWaterTreatments(game, city, amount) <= 0) {
            return;
        }

        IInfection infection = infectionManagement.findInfection(city, plagueName);
        IPlague plague = game.getPlagueRepository()
                             .getPlagueByName(plagueName);

        increaseInfectionSeverity(game, infection, plague, amount, city, triggerEscalation);
    }

    /**
     * Validates the parameters for the infection process.
     *
     * @param game       the game instance
     * @param city       the city to infect
     * @param plagueName the name of the plague to infect the city with
     * @param amount     the amount of infection cubes to add
     * @throws CityManagementException if any parameter is invalid
     */
    private void validateParameters(
            IGame game,
            ICity city,
            PlagueName plagueName,
            int amount
    ) throws CityManagementException {
        if (game == null || city == null || plagueName == null || amount < 0) {
            throw new CityManagementException("Invalid parameters");
        }
    }

    /**
     * Finds the city corresponding to the given infection card.
     *
     * @param game          the game instance
     * @param infectionCard the infection card representing the city to find
     * @return the city corresponding to the infection card
     * @throws CityManagementException if the city is not found
     */
    private ICity findCity(IGame game, InfectionCard infectionCard) throws CityManagementException {
        return Optional.ofNullable(game.getCityRepository()
                                       .getCityByName(infectionCard.getCity()
                                                                   .getName()))
                       .orElseThrow(() -> new CityManagementException("City not found"));
    }

    /**
     * Checks if the infection severity of a city has exceeded the threshold.
     *
     * @param city the city to check
     * @return true if the infection severity has exceeded the threshold, false otherwise
     */
    private boolean hasExceededSeverity(ICity city) {
        return city.getInfections()
                   .stream()
                   .anyMatch(i -> i.getPlague()
                                   .getName()
                                   .equals(city.getPlagueName()) && i.getSeverity() > 3);
    }

    /**
     * Increases the infection severity of a city and handles escalation if necessary.
     *
     * @param game              the game instance
     * @param infection         the infection to increase the severity of
     * @param plague            the plague causing the infection
     * @param amount            the amount to increase the infection severity by
     * @param city              the city to increase the infection severity in
     * @param triggerEscalation whether to trigger escalation if the infection severity exceeds the threshold
     */
    private void increaseInfectionSeverity(
            IGame game,
            IInfection infection,
            IPlague plague,
            int amount,
            ICity city,
            boolean triggerEscalation
    ) {
        int newSeverity = Math.min(infection.getSeverity() + amount, 3);
        int cubesUsed = newSeverity - infection.getSeverity();
        boolean wouldEscalate = infection.getSeverity() + amount > 3;

        infection.setSeverity(newSeverity);
        plague.setCubesRemaining(plague.getCubesRemaining() - cubesUsed);

        if (wouldEscalate && triggerEscalation) {
            escalation(game, city.getName(), List.of(city.getName()), city.getPlagueName());
        }

        if (new PlagueManagement().isCubeCountNegative(game, plague.getName())) {
            // GameOver auslösen (Implementierung mit Issue #137)
        }
    }

    /**
     * Handles the escalation process for a city and its connected cities.
     *
     * @param game            the game instance
     * @param cityName        the name of the city to escalate
     * @param escalatedCities the list of cities that have already escalated
     * @param plagueName      the name of the plague causing the escalation
     */
    public void escalation(IGame game, CityName cityName, List<CityName> escalatedCities, PlagueName plagueName) {
        Queue<CityName> citiesToProcess = new LinkedList<>();
        citiesToProcess.add(cityName);

        while (!citiesToProcess.isEmpty()) {
            CityName currentCity = citiesToProcess.poll();
            List<CityName> connectedCityNames = game.getConnectionRepository()
                                                    .getCityNamesOfConnectedCitiesByCityName(currentCity);
            List<ICity> connectedCities = game.getCityRepository()
                                              .getCitiesByNames(connectedCityNames);

            for (ICity connectedCity : connectedCities) {
                if (escalatedCities.contains(connectedCity.getName())) {
                    continue;
                }

                infectCity(game, connectedCity, plagueName, 1, false);

                if (hasExceededSeverity(connectedCity)) {
                    escalatedCities.add(connectedCity.getName());
                    citiesToProcess.add(connectedCity.getName());
                }
            }
        }
    }
}