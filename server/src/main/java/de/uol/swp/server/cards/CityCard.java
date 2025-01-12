package de.uol.swp.server.cards;

import de.uol.swp.common.cards.CardType;
import de.uol.swp.server.city.City;
import lombok.Getter;


@Getter

public class CityCard extends Card {
    private final City city;

    public CityCard(int id, String title, City city) {
        super(id, title, CardType.CITY_CARD);
        this.city = city;
    }
}
