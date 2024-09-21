package de.uol.swp.server.city;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.server.plague.PlagueName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class City {
    private final PlagueName plagueName;
    private final CityName name;
    private final int foundationDate;
    private final boolean isHarbourCity;
    @Setter
    private boolean hasHospital;

    /**
     * Converts this City object to a Data Transfer Object (DTO).
     *
     * @return an instance of ICityDTO representing this City.
     */
    public ICityDTO toDto() {
        return new CityDTO(plagueName.name(), name.getDisplayName(), foundationDate, isHarbourCity, hasHospital);
    }

    /**
     * Creates a City object from a Data Transfer Object (DTO).
     *
     * @param dto the Data Transfer Object representing a City.
     * @return a new City instance based on the provided DTO.
     */
    public static City fromDto(ICityDTO dto) {
        return new City(
                PlagueName.valueOf(dto.getPlagueName()),
                CityName.fromDisplayName(dto.getName()),
                dto.getFoundationDate(),
                dto.isHarbourCity(),
                dto.isHospitalBuild()
        );
    }

    @Override
    public boolean equals(Object object) {
        boolean equals = false;
        if (object instanceof City city) {
            equals = this.plagueName.equals(city.plagueName) && this.name.equals(city.name) && this.foundationDate == city.foundationDate && this.isHarbourCity == city.isHarbourCity && this.hasHospital == city.hasHospital;
        }
        return equals;
    }

    @Override
    public int hashCode() {
        return plagueName.hashCode() + name.hashCode() + foundationDate + (isHarbourCity ? 1 : 0) + (hasHospital ? 1 : 0);
    }

}
