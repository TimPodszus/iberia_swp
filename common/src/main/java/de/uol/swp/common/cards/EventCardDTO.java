package de.uol.swp.common.cards;


import lombok.Getter;


@Getter

public class EventCardDTO extends CardDTO {
    private final String action;

    protected EventCardDTO(int id, String title, String action) {
        super(id, title, CardType.EVENT_CARD);
        this.action = action;
    }
}
