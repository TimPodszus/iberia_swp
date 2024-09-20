package de.uol.swp.common.city;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CityDTO implements ICityDTO {
    private final String plagueName;
    private final String name;
    private final int foundationDate;
    private final boolean harbourCity;
    private boolean hospitalBuild;
}
