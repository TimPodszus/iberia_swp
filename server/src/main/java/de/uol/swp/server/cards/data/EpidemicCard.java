package de.uol.swp.server.cards.data;


import de.uol.swp.common.cards.CardType;
import de.uol.swp.server.cards.data.Card;
import lombok.Getter;


@Getter

public class EpidemicCard extends Card {
    private final String description;

    public EpidemicCard(int id, String title, String description) {
        super(id, title, CardType.EPIDEMIC_CARD);
        this.description = description;
    }
}
