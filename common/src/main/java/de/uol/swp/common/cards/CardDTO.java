package de.uol.swp.common.cards;

import lombok.Getter;

import java.io.Serializable;

@Getter
public abstract class CardDTO implements Serializable {
    private final int id;
    private final String title;
    private final String type;

    protected CardDTO(int id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }
}
