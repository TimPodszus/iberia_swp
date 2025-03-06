package de.uol.swp.server.plague.management;

import com.google.inject.Inject;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.AbstractManagement;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.states.EndGameState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.TreatExtraPlagueState;
import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.plague.data.IPlague;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.region.data.IRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The PlagueManagement class implements the logic for managing plague-related actions within the game.
 * This includes researching plagues by fulfilling specific conditions such as having the required cards,
 * being in the correct city, and having a hospital built in that city.
 */
public class PlagueManagement extends AbstractManagement implements IPlagueManagement {
    private final IPlayerManagement playerManagement;
    private static final Logger LOG = LoggerFactory.getLogger(PlagueManagement.class);

    /**
     * Constructs a new PlagueManagement instance and initializes the game store.
     * The game store is retrieved as a singleton instance to manage the state and data of the game.
     */
    @Inject
    public PlagueManagement(IPlayerManagement playerManagement) {
        this.playerManagement = playerManagement;
    }

    @Override
    public void researchPlague(IGame game) throws PlagueManagementException, IllegalGameStateException, GameException {
        LOG.debug("Starting plague research in game {}", game.getGameId());
        if (game.getState() instanceof PlayerTurnState playerTurnState) {
            PlagueName plagueToResearch = getPlagueToResearch(game);
            if (plagueToResearch == null) {
                LOG.error("No valid plague to research found.");
                throw new PlagueManagementException("The plague to be researched was not specified");
            }

            LOG.debug("Plague to research: {}", plagueToResearch);
            IPlague plague = game.getPlagueRepository()
                                 .getPlagues()
                                 .stream()
                                 .filter(p -> p.getName()
                                               .equals(plagueToResearch))
                                 .findFirst()
                                 .orElseThrow(() -> new PlagueManagementException("Plague not found"));

            if (plague.isResearched()) {
                LOG.error("Plague {} is already researched.", plagueToResearch);
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
                LOG.error("Player does not have enough cards to research the plague.");
                throw new PlagueManagementException("Player has not enough cards to research the plague");
            }

            ICity currentCity = game.getCurrentPlayer()
                                    .getCurrentPosition();
            LOG.debug("Player is in city: {}", currentCity.getName());

            if (!currentCity.isHospitalBuilt() || !currentCity.getPlagueName()
                                                              .equals(plagueToResearch)) {
                LOG.warn(
                        "No suitable hospital in {} for researching plague {}",
                        currentCity.getName(),
                        plagueToResearch
                );
                throw new PlagueManagementException("No suitable hospital in the current city to research the plague");
            }

            List<CityCard> cardsToDiscard = plagueCards.subList(0, 5);

            playerManagement.discardPlayerCards(
                    game.getGameId(),
                    game.getCurrentPlayer()
                        .getUser()
                        .getUsername(),
                    List.copyOf(cardsToDiscard.stream()
                                              .map(CityCard::getId)
                                              .toList())
            );
            plague.setResearched(true);
            allPlaguesResearched(game);

            playerTurnState.reduceActionsRemaining(game);

        } else {
            LOG.error("Invalid game state {} for researching a plague.", game.getState());
            throw new IllegalGameStateException("Invalid game state for treating plague.");
        }

    }

    /**
     * Determines the plague that the current player can research based on their city cards.
     * A plague can be researched if the player holds at least 5 city cards associated with that plague.
     *
     * @param game the current game instance
     * @return the name of the plague that can be researched, or {@code null} if no plague qualifies
     */
    private PlagueName getPlagueToResearch(IGame game) {
        LOG.debug("Determining plague to research for player in game {}", game.getGameId());
        Map<PlagueName, List<CityCard>> cardsByPlague = game.getCurrentPlayer()
                                                            .getCards()
                                                            .stream()
                                                            .filter(CityCard.class::isInstance)
                                                            .map(CityCard.class::cast)
                                                            .collect(Collectors.groupingBy(cityCard -> cityCard.getCity()
                                                                                                               .getPlagueName()));

        Optional<PlagueName> plagueToResearch = cardsByPlague.entrySet()
                                                             .stream()
                                                             .filter(entry -> entry.getValue()
                                                                                   .size() >= 5)
                                                             .map(Map.Entry::getKey)
                                                             .findFirst();
        return plagueToResearch.orElse(null);
    }

    /**
     * Checks if all plagues have been researched in the current game.
     * If all plagues have been researched, the game state is transitioned to the end game state.
     *
     * @param game the current game instance
     */
    public void allPlaguesResearched(IGame game) {
        LOG.debug("Checking if all plagues have been researched in game {}", game.getGameId());
        boolean allResearched = game.getPlagueRepository()
                                    .getPlagues()
                                    .stream()
                                    .allMatch(IPlague::isResearched);
        if (allResearched) {
            LOG.info("All plagues researched! Transitioning to EndGameState.");
            game.setState(new EndGameState(true));
        }
    }

    @Override
    public boolean canResearchPlague(IGame game) {
        LOG.debug("Checking if the player can research a plague in game {}", game.getGameId());
        PlagueName selectedPlague = getPlagueToResearch(game);
        if (selectedPlague == null) {
            return false;
        }
        ICity currentCity = game.getCurrentPlayer()
                                .getCurrentPosition();
        return currentCity.isHospitalBuilt() && currentCity.getPlagueName()
                                                           .equals(selectedPlague);
    }


    @Override
    public void treatPlague(
            PlagueName plagueToTreat, ICity city, IGame game
    ) throws IllegalGameStateException, PlagueNotFoundException {
        LOG.debug(
                "Attempting to treat plague {} in city {} for game {}",
                plagueToTreat,
                city.getName(),
                game.getGameId()
        );
        if (plagueToTreat == null || city == null) {
            LOG.error("Invalid input: plague, city, or game cannot be null.");
            throw new IllegalArgumentException("Invalid input: plague, city, or game cannot be null.");
        }
        if (!city.hasPlague(plagueToTreat)) {
            LOG.error("Plague {} is not present in city {}.", plagueToTreat, city.getName());
            throw new PlagueNotFoundException("The selected plague is not present in the city or no cubes to remove.");
        }

        if (game.getState() instanceof PlayerTurnState playerTurnState) {
            LOG.debug("Removing one cube of plague {} from city {}", plagueToTreat, city.getName());
            city.removePlagueCubes(plagueToTreat, 1);
            game.getPlagueRepository()
                .getPlagueByName(plagueToTreat)
                .increaseCubes(1);
            playerTurnState.reduceActionsRemaining(game);
        } else if (game.getState() instanceof TreatExtraPlagueState) {
            LOG.debug("Treating extra plague cube of {} in city {}", plagueToTreat, city.getName());
            city.removePlagueCubes(plagueToTreat, 1);
            game.getPlagueRepository()
                .getPlagueByName(plagueToTreat)
                .increaseCubes(1);
        } else {
            LOG.error(
                    "Invalid game state {} for treating a plague.",
                    game.getState()
                        .getClass()
                        .getSimpleName()
            );
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
}

