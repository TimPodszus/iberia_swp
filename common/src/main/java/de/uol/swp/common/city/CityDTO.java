package de.uol.swp.common.city;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data Transfer Object for City.
 */
@AllArgsConstructor
@Getter
public class CityDTO implements ICityDTO {
    private final int id;
    private final String plagueName;
    private final String name;
    private final int foundationDate;
    private final boolean harbourCity;
    private boolean hospitalBuild;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object instanceof CityDTO cityDTO) {
            return name.equals(cityDTO.getName()) && plagueName.equals(cityDTO.getPlagueName()) && foundationDate == cityDTO.getFoundationDate() && harbourCity == cityDTO.isHarbourCity() && hospitalBuild == cityDTO.isHospitalBuild();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + plagueName.hashCode() + foundationDate + (harbourCity ? 1 : 0) + (hospitalBuild ? 1 : 0);
    }
}
