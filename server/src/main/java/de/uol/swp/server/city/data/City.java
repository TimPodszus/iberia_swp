package de.uol.swp.server.city.data;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.infection.data.IInfection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final Map<PlagueName, Integer> plagueCubes = new HashMap<>();

    public void removePlagueCubes(PlagueName plagueName, int count) {
        int currentCount = plagueCubes.getOrDefault(plagueName, 0);
        if (currentCount < count) {
            throw new IllegalArgumentException("Not enough plague cubes to remove.");
        }
        plagueCubes.put(plagueName, currentCount - count);
    }
    public boolean hasPlague(PlagueName plagueName) {
        return plagueCubes.getOrDefault(plagueName, 0) > 0;
    }
    public int getPlagueCubes(PlagueName plagueName) {
        return plagueCubes.getOrDefault(plagueName, 0);
    }
}