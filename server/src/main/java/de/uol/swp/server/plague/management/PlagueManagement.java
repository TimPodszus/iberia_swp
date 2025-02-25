package de.uol.swp.server.plague.management;

import com.google.inject.Inject;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.states.EndGameState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.TreatExtraPlagueState;
import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.plague.data.IPlague;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.region.data.IRegion;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The PlagueManagement class implements the logic for managing plague-related actions within the game.
 * This includes researching plagues by fulfilling specific conditions such as having the required cards,
 * being in the correct city, and having a hospital built in that city.
 */
public class PlagueManagement extends AbstractManagement implements IPlagueManagement {
    private final IPlayerManagement playerManagement;

    /**
     * Constructs a new PlagueManagement instance and initializes the game store.
     * The game store is retrieved as a singleton instance to manage the state and data of the game.
     */
    @Inject
    public PlagueManagement(IPlayerManagement playerManagement) {
        this.playerManagement = playerManagement;
    }

    /**
     * Researches the specified plague in the current game. This method checks if the player has the required cards,
     * if they are in a city with a hospital built, and if the hospital is in a city affected by the plague to be researched.
     * Once the plague is researched, it is marked as such and the appropriate game state transitions occur.
     *
     * @param plagueToResearch the plague to be researched. Must not be null.
     * @param game             the current game instance where the plague research is being performed.
     * @throws PlagueManagementException if the plague to be researched is not specified, if the player does not have enough cards,
     *                                   if the plague has already been researched, or if the city does not have a suitable hospital.
     */
    @Override
    public void researchPlague(PlagueName plagueToResearch, Game game) throws PlagueManagementException {
        if (plagueToResearch == null) {
            throw new PlagueManagementException("The plague to be researched was not specified");
        }

        IPlague plague = game.getPlagueRepository()
                .getPlagues()
                .stream()
                .filter(p -> p.getName()
                        .equals(plagueToResearch))
                .findFirst()
                .orElseThrow(() -> new PlagueManagementException("Plague not found"));

        if (plague.isResearched()) {
            throw new PlagueManagementException("The plague is already researched");
        }
        Map<PlagueName, List<CityCard>> cardsByPlague = game.getCurrentPlayer()
                .getCards()
                .stream()
                .filter(CityCard.class::isInstance)
                .map(CityCard.class::cast)
                .collect(Collectors.groupingBy(cityCard -> cityCard.getCity()
                        .getPlagueName()));

        List<CityCard> plagueCards = cardsByPlague.get(plagueToResearch);

        if (plagueCards == null || plagueCards.size() < 5) {
            throw new PlagueManagementException("Player has not enough cards to research the plague");
        }

        ICity currentCity = game.getCurrentPlayer()
                .getCurrentPosition();

        if (!currentCity.isHospitalBuilt() || !currentCity.getPlagueName()
                .equals(plagueToResearch)) {
            throw new PlagueManagementException("No suitable hospital in the current city to research the plague");
        }

        List<CityCard> cardsToDiscard = plagueCards.subList(0, 5);

        playerManagement.discardCards(game.getGameId(), game.getCurrentPlayer(), cardsToDiscard);

        plague.setResearched(true);
        allPlaguesResearched(game);
    }

    /**
     * Checks if all plagues have been researched in the current game.
     * If all plagues have been researched, the game state is transitioned to the end game state.
     *
     * @param game the current game instance
     */
    public void allPlaguesResearched(Game game) {
        boolean allResearched = game.getPlagueRepository()
                .getPlagues()
                .stream()
                .allMatch(IPlague::isResearched);
        if (allResearched) {
            game.setState(new EndGameState(true));
        }
    }

    /**
     * Treats a plague in the specified city by removing one plague cube.
     * Ensures that the input parameters are valid and that the city contains the plague.
     * If the player is not a country doctor, an action is deducted from their turn.
     *
     * @param plagueToTreat   The plague to be treated.
     * @param city            The city where the plague is treated.
     * @param game            The current game instance.
     * @throws PlagueManagementException If the plague is not present in the city or no cubes remain.
     * @throws IllegalArgumentException  If any of the input parameters are null.
     */
    @Override
    public void treatPlague(PlagueName plagueToTreat, ICity city, IGame game) throws PlagueManagementException {
        if (plagueToTreat == null || city == null) {
            throw new IllegalArgumentException("Invalid input: plague, city, or game cannot be null.");
        }
        if (!city.hasPlague(plagueToTreat) || city.getPlagueCubes(plagueToTreat) == 0) {
            throw new PlagueManagementException("The selected plague is not present in the city or no cubes to remove.");
        }

        if (game.getState() instanceof PlayerTurnState playerTurnState) {
            city.removePlagueCubes(plagueToTreat, 1);
            game.getPlagueRepository().getPlagueByName(plagueToTreat).increaseCubes(1);
            playerTurnState.reduceActionsRemaining(game);
        } else if (game.getState() instanceof TreatExtraPlagueState) {
            city.removePlagueCubes(plagueToTreat, 1);
            game.getPlagueRepository().getPlagueByName(plagueToTreat).increaseCubes(1);
            game.setState(game.getPreviousState());
        } else {
            throw new PlagueManagementException("Invalid game state for treating plague.");
        }
    }

    /**
     * Retrieves the Infectious Diseases in the specified city.
     * Filters the infections in the city to only include those with a severity of 1 or higher.
     * @param game    the current game instance.
     * @param cityId  the ID of the city to retrieve infections from.
     * @return a list of infections in the city with a severity of 1 or higher.
     */
    @Override
    public List<IInfection> getInfectionsInCity(IGame game, int cityId) {
        ICity city = game.getCityRepository().getCity(cityId);
        return city.getInfections()
                   .stream()
                   .filter(infection -> infection.getSeverity() >= 1)
                   .toList();
    }

    @Override
    public List<ICity> getCitiesNearBy(IGame game, ICity currentCity) {
        List<IRegion> allRegions = game.getRegionRepository()
                                       .getRegions();
        List<IRegion> regionsNearBy = allRegions.stream()
                                                .filter(region -> region.getSurroundingCities()
                                                                        .contains(currentCity))
                                                .toList();
        return regionsNearBy.stream()
                            .flatMap(region -> game.getRegionRepository()
                                                   .getCitiesInAdjacentRegions(region)
                                                   .stream())
                            .filter(city -> city.getInfections()
                                                .stream()
                                                .anyMatch(infection -> infection.getSeverity() >= 1))
                            .toList();
    }

    public boolean isCubeCountNegative(IGame game, PlagueName plagueName) {
        return game.getPlagueRepository()
                   .getPlagueByName(plagueName)
                   .getCubesRemaining() > 0;
    }
}

