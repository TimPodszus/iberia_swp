package de.uol.swp.server.cards;


import lombok.Getter;


@Getter

public class EpidemicCard extends Card {
    private final String description;

    public EpidemicCard(int id, String title, String type, String description)
    {
        super(id, title, type);
        this.description = description;
    }
}
