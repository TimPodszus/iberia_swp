package de.uol.swp.server.cards.data;


import de.uol.swp.common.cards.data.CardType;
import lombok.Getter;


@Getter

public class EpidemicCard extends Card {
    private final String description;

    public EpidemicCard(int id, String title, String description) {
        super(id, title, CardType.EPIDEMIC_CARD);
        this.description = description;
    }
}
