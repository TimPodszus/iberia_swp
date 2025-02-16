package de.uol.swp.server.cards.data;

import de.uol.swp.common.cards.data.CardType;
import de.uol.swp.server.city.data.ICity;
import lombok.Getter;


@Getter

public class InfectionCard extends Card {
    private final ICity city;

    public InfectionCard(int id, String title, ICity city) {
        super(id, title, CardType.INFECTION_CARD);
        this.city = city;
    }
}
