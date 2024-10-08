package de.uol.swp.common.cards;

import lombok.Getter;

@Getter
public abstract class CardDTO {
    private final int id;
    private final String title;
    private final String type;

    protected CardDTO(int id, String title, String type)
    {
        this.id = id;
        this.title = title;
        this.type = type;
    }


}
