package de.uol.swp.server.city;

import de.uol.swp.common.game.PlagueName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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

    @Override
    public boolean equals(Object object) {
        boolean equals = false;
        if (object instanceof City city) {
            equals = this.plagueName.equals(city.plagueName) && this.name.equals(city.name) && this.foundationDate == city.foundationDate && this.harbourCity == city.harbourCity && this.hospitalBuilt == city.hospitalBuilt;
        }
        return equals;
    }

    @Override
    public int hashCode() {
        return plagueName.hashCode() + name.hashCode() + foundationDate + (harbourCity ? 1 : 0) + (hospitalBuilt ? 1 : 0);
    }
}