package de.uol.swp.common.cards;


import lombok.Getter;


@Getter

public class EpidemicCardDTO extends CardDTO {
    private final String description;

    public EpidemicCardDTO(int id, String title, String description) {
        super(id, title, CardType.EPIDEMIC_CARD);
        this.description = description;
    }
}
