package de.uol.swp.common.cards;

import de.uol.swp.common.city.CityDTO;
import lombok.Getter;


@Getter

public class InfectionCardDTO extends CardDTO {
    private final CityDTO city;

    public InfectionCardDTO(int id, String title, CityDTO city) {
        super(id, title, CardType.INFECTION_CARD);
        this.city = city;
    }
}
