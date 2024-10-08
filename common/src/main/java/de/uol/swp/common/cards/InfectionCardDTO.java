package de.uol.swp.common.cards;

import de.uol.swp.common.city.CityDTO;
import lombok.Getter;


@Getter

public class InfectionCardDTO extends CardDTO {
    private final CityDTO city;

    public InfectionCardDTO(int id, String title, String type, CityDTO city)
    {
        super(id, title, type);
        this.city = city;
    }
}
