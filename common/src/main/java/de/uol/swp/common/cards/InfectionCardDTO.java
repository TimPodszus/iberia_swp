package de.uol.swp.common.cards;

import de.uol.swp.common.city.ICityDTO;
import lombok.Getter;

/**
 * Represents an infection card in the game.
 * This card is associated with a specific city.
 */
@Getter
public class InfectionCardDTO extends CardDTO {
    private final ICityDTO city;

    /**
     * Constructs a new InfectionCardDTO.
     *
     * @param id    the unique identifier of the card
     * @param title the title of the card
     * @param city  the city associated with the infection card
     */
    public InfectionCardDTO(int id, String title, ICityDTO city) {
        super(id, title, CardType.INFECTION_CARD);
        this.city = city;
    }
}
