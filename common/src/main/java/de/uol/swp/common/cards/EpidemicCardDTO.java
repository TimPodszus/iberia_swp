package de.uol.swp.common.cards;


import lombok.Getter;


@Getter

public class EpidemicCardDTO extends CardDTO {
    private final String description;

    public EpidemicCardDTO(int id, String title, CardType type, String description) {
        super(id, title, type);
        this.description = description;
    }
}
