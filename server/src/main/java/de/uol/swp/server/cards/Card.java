package de.uol.swp.server.cards;

import lombok.Getter;

@Getter
public abstract class Card {
    private final int id;
    private final String title;
    private final String type;

    protected Card(int id, String title, String type)
    {
        this.id = id;
        this.title = title;
        this.type = type;
    }


}
