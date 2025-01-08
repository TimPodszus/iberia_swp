package de.uol.swp.server.city.management;

import de.uol.swp.server.cards.InfectionCard;
import de.uol.swp.server.city.City;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.game.store.IGameStore;

public class CityManagement implements ICityManagement {

    IGameStore gameStore = GameStore.getInstance();

    public void infectCity(InfectionCard infectionCard, int amount) {
        //Todo: Ticket InfectCity
    }

    public City getCity(String lobbyId, String cityId) {
        return gameStore.getGame(lobbyId)
                        .getCityRepository()
                        .getCity(cityId);
    }
}
