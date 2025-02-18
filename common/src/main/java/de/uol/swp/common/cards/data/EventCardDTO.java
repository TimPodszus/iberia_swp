package de.uol.swp.common.cards.data;

import lombok.Getter;

/**
 * Represents an event card data transfer object.
 */
@Getter
public class EventCardDTO extends CardDTO {
    private final String action;

    /**
     * Constructs a new EventCardDTO.
     *
     * @param id     the unique identifier of the card
     * @param title  the title of the card
     * @param action the action associated with the event card
     */
    public EventCardDTO(int id, String title, String action) {
        super(id, title, CardType.EVENT_CARD);
        this.action = action;
    }
}
