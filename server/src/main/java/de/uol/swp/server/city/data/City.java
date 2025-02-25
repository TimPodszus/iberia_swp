package de.uol.swp.server.city.data;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.infection.data.Infection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a city in the game, with attributes such as plague name, city name, foundation date,
 * whether it is a harbour city, and whether a hospital has been built.
 */
@AllArgsConstructor
@Getter
public class City implements ICity {

    /**
     * Constructs a new City with the specified attributes.
     *
     * @param id             the id of the city
     * @param plagueName     the name of the plague affecting the city
     * @param name           the name of the city
     * @param foundationDate the foundation date of the city
     * @param harbourCity    indicates if the city is a harbour city
     */
    public City(int id, PlagueName plagueName, CityName name, int foundationDate, boolean harbourCity) {
        this.id = id;
        this.plagueName = plagueName;
        this.name = name;
        this.foundationDate = foundationDate;
        this.harbourCity = harbourCity;
        Arrays.stream(PlagueName.values())
              .forEach(plagueNameValue -> this.infections.add(new Infection(0, plagueNameValue)));
    }

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

    /**
     * Removes a specified number of plague cubes from the infections of the given plague.
     *
     * @param plagueName the name of the plague from which cubes should be removed.
     * @param amount the number of plague cubes to remove.
     * @throws IllegalArgumentException if there are not enough plague cubes to remove.
     */
    public void removePlagueCubes(PlagueName plagueName, int amount) {
        for (IInfection infection : infections) {
            if (infection.getPlagueName().equals(plagueName)) {
                infection.decreaseSeverity(amount);
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
            hasPlague = infection.getPlagueName().equals(plagueName);
            if(hasPlague) {
                break;
            }
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
            if (infection.getPlagueName().equals(plagueName)) {
                plagueCubes = infection.getSeverity();
            }
        }
        return plagueCubes;
    }
}