package de.uol.swp.server.cards;

import de.uol.swp.common.cards.CardType;
import de.uol.swp.server.city.data.ICity;
import lombok.Getter;


@Getter

public class InfectionCard extends Card {
    private final ICity city;

    public InfectionCard(int id, String title, CardType type, ICity city) {
        super(id, title, type);
        this.city = city;
    }
}
