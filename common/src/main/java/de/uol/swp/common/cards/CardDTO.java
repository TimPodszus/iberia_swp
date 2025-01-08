package de.uol.swp.common.cards;

import lombok.Getter;

import java.io.Serializable;

@Getter
public abstract class CardDTO implements ICardDTO, Serializable {
    private final int id;
    private final String title;
    private final CardType type;

    protected CardDTO(int id, String title, CardType type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }
}
