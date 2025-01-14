package de.uol.swp.common.cards;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.ICityDTO;
import lombok.Getter;


@Getter

public class CityCardDTO extends CardDTO {
    private final ICityDTO city;

    public CityCardDTO(int id, String title, ICityDTO city) {
        super(id, title, CardType.CITY_CARD);
        this.city = city;
    }
}
