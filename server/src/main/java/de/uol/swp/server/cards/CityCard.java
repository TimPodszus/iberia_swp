package de.uol.swp.server.cards;

import de.uol.swp.server.city.City;

import lombok.Getter;


@Getter

public class CityCard extends Card
{
    private final City city;

    protected CityCard(int id, String title, String type, City city)
    {
        super(id, title, type);
        this.city = city;
    }
}
