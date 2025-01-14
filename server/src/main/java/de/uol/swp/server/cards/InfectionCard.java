package de.uol.swp.server.cards;

import de.uol.swp.common.cards.CardType;
import de.uol.swp.server.city.City;
import lombok.Getter;


@Getter

public class InfectionCard extends Card {
    private final City city;

    public InfectionCard(int id, String title, City city) {
        super(id, title, CardType.INFECTION_CARD);
        this.city = city;
    }
}
