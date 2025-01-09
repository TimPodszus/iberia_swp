package de.uol.swp.server.city;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.infection.Infection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents a city in the game, with attributes such as plague name, city name, foundation date,
 * whether it is a harbour city, and whether a hospital has been built.
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class City {
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
     * The infections in the city.
     */
    @Setter
    private List<Infection> infections;
}