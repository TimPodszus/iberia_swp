package de.uol.swp.server.cards;


import de.uol.swp.common.cards.CardType;
import lombok.Getter;


@Getter

public class EpidemicCard extends Card {
    private final String description;

    public EpidemicCard(int id, String title, CardType type, String description) {
        super(id, title, type);
        this.description = description;
    }
}
