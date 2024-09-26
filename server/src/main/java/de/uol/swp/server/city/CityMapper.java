package de.uol.swp.server.city;

import de.uol.swp.common.city.ICityDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CityMapper {
    public static ICityDTO mapToDTO(City city) {
        return city.toDto();
    }

    public static City mapFromDTO(ICityDTO dto) {
        return CityRepository.getCityByName(CityName.fromDisplayName(dto.getName()));
    }
}
