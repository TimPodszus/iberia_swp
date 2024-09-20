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
        return new CityDTO(name.getDisplayName(), plagueName.name(), foundationDate, isHarbourCity, hasHospital);
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
}
