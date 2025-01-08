package de.uol.swp.server.cards;

import de.uol.swp.common.cards.CardType;
import lombok.Getter;

@Getter
public abstract class Card {
    private final int id;
    private final String title;
    private final CardType type;

    protected Card(int id, String title, CardType type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }
}
