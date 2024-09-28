package de.uol.swp.server.city;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.enums.PlagueName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CityMapper {

    public static ICityDTO toDTO(City city) {
        return new CityDTO(
                city.getId(),
                city.getPlagueName()
                    .name(),
                city.getName()
                    .getDisplayName(),
                city.getFoundationDate(),
                city.isHarbourCity(),
                city.isHasHospital()
        );
    }

    public static City fromDTO(ICityDTO cityDTO) {
        return new City(
                cityDTO.getId(),
                PlagueName.valueOf(cityDTO.getPlagueName()),
                CityName.getCityByName(cityDTO.getName()),
                cityDTO.getFoundationDate(),
                cityDTO.isHarbourCity(),
                cityDTO.isHospitalBuild()
        );
    }
}
