package de.uol.swp.common.cards;

import de.uol.swp.common.city.CityDTO;
import lombok.Getter;


@Getter

public class CityCardDTO extends CardDTO {
    private final CityDTO city;

    public CityCardDTO(int id, String title, CityDTO city) {
        super(id, title, CardType.CITY_CARD);
        this.city = city;
    }
}
