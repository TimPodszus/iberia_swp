package de.uol.swp.common.cards;

import de.uol.swp.common.city.ICityDTO;
import lombok.Getter;


@Getter

public class InfectionCardDTO extends CardDTO {
    private final ICityDTO city;

    public InfectionCardDTO(int id, String title, ICityDTO city) {
        super(id, title, CardType.INFECTION_CARD);
        this.city = city;
    }
}
