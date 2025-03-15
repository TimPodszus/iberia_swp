package de.uol.swp.common.cards.data;

import lombok.Getter;

/**
 * Represents an Epidemic Card Data Transfer Object (DTO).
 * This class extends the CardDTO class and includes an additional description field.
 */
@Getter
public class EpidemicCardDTO extends CardDTO {
    private final String description;

    /**
     * Constructs a new EpidemicCardDTO with the specified id, title, and description.
     *
     * @param id          the unique identifier of the card
     * @param title       the title of the card
     * @param description the description of the epidemic card
     */
    public EpidemicCardDTO(int id, String title, String description) {
        super(id, title, CardType.EPIDEMIC_CARD);
        this.description = description;
    }
}