package de.uol.swp.server.plague;

import com.google.inject.Inject;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.cards.CityCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.game.store.IGameStore;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlagueManagement implements IPlagueManagement {

    private final IGameStore gameStore;

    /**
     * Constructs a new PlagueManagement instance and initializes the game store.
     *
     */
    @Inject
    public PlagueManagement() {
        this.gameStore = GameStore.getInstance();
    }

    @Override
    public void researchPlague(PlagueName plagueToResearch, Game game) throws PlagueManagementException {
        if (plagueToResearch == null) {
            throw new PlagueManagementException("Die zu erforschende Seuche wurde nicht angegeben.");
        }

        Plague plague = game.getPlagueRepository().getPlagues().stream().filter(p -> p.getName().equals(plagueToResearch)).findFirst().orElseThrow(() -> new PlagueManagementException("Plague not found"));

        if (plague.isResearched()) {
            throw new PlagueManagementException("Plague already reserached");
        }
        Map<PlagueName, List<CityCard>> cardsByPlague = game.getCurrentPlayer().getCards().stream()
                .filter(card -> card instanceof CityCard)
                .map(card -> (CityCard) card)
                .collect(Collectors.groupingBy(cityCard -> cityCard.getCity().getPlagueName()));

        List<CityCard> plagueCards = cardsByPlague.get(plagueToResearch);

        if (plagueCards == null || plagueCards.size() < 5) {
            throw new PlagueManagementException("Player has not enough cars to research the plague");
        }

        City currentCity = game.getCurrentPlayer()
                .getCurrentPosition();

        if (!currentCity.isHospitalBuilt() || !currentCity.getPlagueName().equals(plagueToResearch)) {
            throw new IllegalStateException("No suitable hospital in the current city to research the plague");
        }


        List<CityCard> cardsToDiscard = plagueCards.subList(0, 5);
        for (CityCard card : cardsToDiscard) {
            game.getCurrentPlayer().discardCard(card);
            game.getPlayerCardDiscardPile().add(card);
        }

        plague.setResearched(true);
        game.getState().handleAction(game, game.getCurrentPlayer());
    }
}

