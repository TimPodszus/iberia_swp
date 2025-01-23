package de.uol.swp.server.city.data;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.infection.data.IInfection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a city in the game, with attributes such as plague name, city name, foundation date,
 * whether it is a harbour city, and whether a hospital has been built.
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class City implements ICity {
    /**
     * The id of the city.
     */
    private final int id;
    /**
     * The name of the plague affecting the city.
     */
    private final PlagueName plagueName;

    /**
     * The name of the city.
     */
    private final CityName name;

    /**
     * The foundation date of the city.
     */
    private final int foundationDate;

    /**
     * Indicates if the city is a harbour city.
     */
    private final boolean harbourCity;

    /**
     * Indicates if a hospital has been built in the city.
     */
    @Setter
    private boolean hospitalBuilt;

    /**
     * The list of infections in the city.
     */
    @Setter
    private List<IInfection> infections = new ArrayList<>();

    /**
     * Removes a specified number of plague cubes from the infections of the given plague.
     *
     * @param plagueName the name of the plague from which cubes should be removed.
     * @param count the number of plague cubes to remove.
     * @throws IllegalArgumentException if there are not enough plague cubes to remove.
     */
    public void removePlagueCubes(PlagueName plagueName, int count) {
        int currentCount;
        for (IInfection infection : infections) {
            if (infection.getPlague().getName().equals(plagueName)) {
                currentCount = infection.getSeverity();
                if (currentCount < count) {
                    throw new IllegalArgumentException("Not enough plague cubes to remove.");
                }
                infection.setSeverity(currentCount - count);
                infection.getPlague().setCubesRemaining(infection.getPlague().getCubesRemaining() - count);
            }
        }
    }

    /**
     * Checks if a plague with the specified name exists in the current infections.
     *
     * @param plagueName the name of the plague to check.
     * @return {@code true} if the plague exists, {@code false} otherwise.
     */
    public boolean hasPlague(PlagueName plagueName) {
        boolean hasPlague = false;
        for (IInfection infection : infections) {
            hasPlague = infection.getPlague().getName().equals(plagueName);
        }
        return hasPlague;
    }

    /**
     * Retrieves the number of plague cubes for a specific plague.
     *
     * @param plagueName the name of the plague for which to get the cube count.
     * @return the number of cubes associated with the specified plague. If the plague is not found, returns 0.
     */
    public int getPlagueCubes(PlagueName plagueName) {
        int plagueCubes = 0;
        for (IInfection infection : infections) {
            if (infection.getPlague().getName().equals(plagueName)) {
                plagueCubes = infection.getSeverity();
            }
        }
        return plagueCubes;
    }
}