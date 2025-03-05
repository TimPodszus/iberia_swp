package de.uol.swp.server.plague.management;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.infection.data.IInfection;

import java.util.List;

public interface IPlagueManagement {
    /**
     * Researches a plague in the game if the player meets the necessary conditions.
     *
     * @param game the current game instance
     * @throws PlagueManagementException if the plague cannot be researched due to game conditions
     */
    void researchPlague(IGame game) throws PlagueManagementException, IllegalGameStateException;

    /**
     * Determines whether a plague can be researched in the given game.
     *
     * @param game The current game instance where the research should be evaluated.
     * @return {@code true} if a plague can be researched, otherwise {@code false}.
     */
    boolean canResearchPlague(IGame game);
    /**
     * Treats a plague in a specified city within the game.
     *
     * @param plagueToTreat the plague to be treated
     * @param city          the city where the plague should be treated
     * @param game          the current game instance
     * @throws PlagueNotFoundException if the plague cannot be treated due to game conditions
     * @throws IllegalGameStateException if the game is in an illegal state
     */
    void treatPlague(PlagueName plagueToTreat, ICity city, IGame game) throws IllegalGameStateException, PlagueNotFoundException;

    /**
     * Retrieves the game instance associated with the given lobby ID.
     *
     * @param lobbyId the ID of the lobby
     * @return the corresponding game instance
     */
    IGame getGame(String lobbyId);

    /**
     * Retrieves a list of infections present in a specified city.
     *
     * @param game   the current game instance
     * @param cityId the ID of the city
     * @return a list of infections in the specified city
     */
    List<IInfection> getInfectionsInCity(IGame game, int cityId);

    /**
     * Retrieves a list of cities that are geographically or strategically near the given city.
     *
     * @param game        the current game instance
     * @param currentCity the city from which nearby cities should be found
     * @return a list of cities near the given city
     */
    List<ICity> getCitiesNearBy(IGame game, ICity currentCity);

    /**
     * Handles the scenario where all plagues in the game have been successfully researched.
     *
     * @param game the current game instance
     */
    void allPlaguesResearched(IGame game);
}