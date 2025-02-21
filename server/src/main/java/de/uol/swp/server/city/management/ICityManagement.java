package de.uol.swp.server.city.management;

import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;

/**
 * Interface for managing city-related operations in the game.
 */
public interface ICityManagement {

    /**
     * Retrieves a city by its lobby ID and city ID.
     *
     * @param lobbyId the lobby ID
     * @param cityId  the city ID
     * @return the city instance
     */
    ICity getCity(String lobbyId, int cityId);

    /**
     * Infects a city with its own plague.
     *
     * @param game          the game instance
     * @param infectionCard the infection card used
     * @param amount        the amount of infection
     */
    void infectCityWithOwnPlague(IGame game, InfectionCard infectionCard, int amount);
}
