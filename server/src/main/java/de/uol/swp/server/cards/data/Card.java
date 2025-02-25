package de.uol.swp.server.cards.data;

import de.uol.swp.common.cards.data.CardType;
import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class Card implements ICard {
    private final int id;
    private final String title;
    private final CardType type;

    protected Card(int id, String title, CardType type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Card card = (Card) object;
        return id == card.id && Objects.equals(title, card.title) && type == card.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, type);
    }

    @Override
    public int getId() {
        return id;
    }
}
