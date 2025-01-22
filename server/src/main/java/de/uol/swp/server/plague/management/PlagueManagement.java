package de.uol.swp.server.plague.management;

import com.google.inject.Inject;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.game.store.IGameStore;
import de.uol.swp.server.plague.data.IPlague;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The PlagueManagement class implements the logic for managing plague-related actions within the game.
 * This includes researching plagues by fulfilling specific conditions such as having the required cards,
 * being in the correct city, and having a hospital built in that city.
 */
public class PlagueManagement implements IPlagueManagement {

    private final IGameStore gameStore;

    /**
     * Constructs a new PlagueManagement instance and initializes the game store.
     * The game store is retrieved as a singleton instance to manage the state and data of the game.
     */
    @Inject
    public PlagueManagement() {
        this.gameStore = GameStore.getInstance();
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
        for (CityCard card : cardsToDiscard) {
            game.getCurrentPlayer()
                .discardCard(card);
            game.getPlayerCardDiscardPile()
                .add(card);
        }

        plague.setResearched(true);
    }

    public boolean isCubeCountNegative(IGame game, PlagueName plagueName) {
        return game.getPlagueRepository()
                   .getPlagueByName(plagueName)
                   .getCubesRemaining() > 0;
    }
}

