package de.uol.swp.server.cards;

import de.uol.swp.common.cards.CardType;
import de.uol.swp.server.city.City;
import lombok.Getter;


@Getter

public class CityCard extends Card {
    private final City city;

    public CityCard(int id, String title, CardType type, City city) {
        super(id, title, type);
        this.city = city;
    }
}
