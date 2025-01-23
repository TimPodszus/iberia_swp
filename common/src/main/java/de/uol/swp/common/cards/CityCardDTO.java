package de.uol.swp.common.cards;

import de.uol.swp.common.city.ICityDTO;
import lombok.Getter;

/**
 * Represents a city card in the game.
 * This class extends the CardDTO class and includes additional information about the city.
 */
@Getter
public class CityCardDTO extends CardDTO {
    private final ICityDTO city;

    /**
     * Constructs a new CityCardDTO.
     *
     * @param id    the unique identifier of the card
     * @param title the title of the card
     * @param city  the city associated with this card
     */
    public CityCardDTO(int id, String title, ICityDTO city) {
        super(id, title, CardType.CITY_CARD);
        this.city = city;
    }
}
