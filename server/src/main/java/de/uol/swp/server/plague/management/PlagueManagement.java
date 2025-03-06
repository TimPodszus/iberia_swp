package de.uol.swp.server.plague.management;

import com.google.inject.Inject;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
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

    @Override
    public void researchPlague(PlagueName plagueToResearch, IGame game) throws PlagueManagementException {
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

    public void allPlaguesResearched(IGame game) {
        boolean allResearched = game.getPlagueRepository()
                                    .getPlagues()
                                    .stream()
                                    .allMatch(IPlague::isResearched);
        if (allResearched) {
            game.setState(new EndGameState(true));
            sendServerMessageEvent(
                    game.getGameId(),
                    "Herzlichen Glückwunsch! Alle Plagen wurden erforscht. Ihr habt das Spiel gewonnen! 🎉"
            );
        }
    }

    @Override
    public void treatPlague(PlagueName plagueToTreat, ICity city, IGame game) throws IllegalGameStateException, PlagueNotFoundException {
        if (plagueToTreat == null || city == null) {
            throw new IllegalArgumentException("Invalid input: plague, city, or game cannot be null.");
        }
        if (!city.hasPlague(plagueToTreat)) {
            throw new PlagueNotFoundException("The selected plague is not present in the city or no cubes to remove.");
        }

        if (game.getState() instanceof PlayerTurnState playerTurnState) {
            city.removePlagueCubes(plagueToTreat, 1);
            game.getPlagueRepository()
                .getPlagueByName(plagueToTreat)
                .increaseCubes(1);
            playerTurnState.reduceActionsRemaining(game);
        } else if (game.getState() instanceof TreatExtraPlagueState) {
            city.removePlagueCubes(plagueToTreat, 1);
            game.getPlagueRepository()
                .getPlagueByName(plagueToTreat)
                .increaseCubes(1);
        } else {
            throw new IllegalGameStateException("Invalid game state for treating plague.");
        }
    }

    @Override
    public List<IInfection> getInfectionsInCity(IGame game, int cityId) {
        ICity city = game.getCityRepository()
                         .getCity(cityId);
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

