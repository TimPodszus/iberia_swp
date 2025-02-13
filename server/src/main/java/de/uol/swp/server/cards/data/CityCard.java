package de.uol.swp.server.cards.data;

import de.uol.swp.common.cards.CardType;
import de.uol.swp.server.cards.data.Card;
import de.uol.swp.server.city.data.ICity;
import lombok.Getter;


@Getter

public class CityCard extends Card {
    private final ICity city;

    public CityCard(int id, String title, ICity city) {
        super(id, title, CardType.CITY_CARD);
        this.city = city;
    }
}
