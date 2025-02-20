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
import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.plague.data.IPlague;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.region.data.IRegion;

import java.util.ArrayList;
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
    @Override
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
     * @param isCountryDoctor Whether the player is a country doctor (special role).
     * @throws PlagueManagementException If the plague is not present in the city or no cubes remain.
     * @throws IllegalArgumentException  If any of the input parameters are null.
     */
    @Override
    public void treatPlague(PlagueName plagueToTreat, ICity city, IGame game, boolean isCountryDoctor) throws PlagueManagementException {
        if (game.getState() instanceof PlayerTurnState playerTurnState) {
            if (plagueToTreat == null || city == null) {
                throw new IllegalArgumentException("Invalid input: plague or city cannot be null.");
            }

            if (!city.hasPlague(plagueToTreat)) {
                throw new PlagueManagementException("The selected plague is not present in the city:" + plagueToTreat);
            }

            int plagueCubes = city.getPlagueCubes(plagueToTreat);
            if (plagueCubes == 0) {
                throw new PlagueManagementException("No plague cubes to remove for the selected plague.");
            }

            city.removePlagueCubes(plagueToTreat, 1);
            if (!isCountryDoctor) {
                playerTurnState.reduceActionsRemaining(game);
            }
        }
    }

    @Override
    public List<IInfection> getInfectionsInCity(IGame game) {
        IPlayer player = game.getCurrentPlayer();
        ICity city = player.getCurrentPosition();
        return city.getInfections();
    }

    @Override
    public List<ICity> getCitiesNearBy(IGame game, ICity currentCity) {
        List<IRegion> allRegions = game.getRegionRepository().getRegions();
        List<IRegion> regionsNearBy = new ArrayList<>();
        for (IRegion region : allRegions) {
            if (region.getSurroundingCities().contains(currentCity)) {
                regionsNearBy.add(region);
            }
        }
        List<ICity> citiesNearBy = new ArrayList<>();
        for (IRegion region : regionsNearBy) {
            List<ICity> citiesInAdjecentRegion = game.getRegionRepository().getCitiesInAdjacentRegions(region);
            citiesNearBy.addAll(citiesInAdjecentRegion);
        }
        return citiesNearBy;
    }

}

