package de.uol.swp.server.city;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.ICityDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import de.uol.swp.server.infection.InfectionMapper;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CityMapper {
    public static ICityDTO toDTO(City city) {
        return new CityDTO(
                city.getId(),
                city.getId(),
                city.getPlagueName(),
                city.getName(),
                city.getFoundationDate(),
                city.isHarbourCity(),
                city.isHospitalBuilt(),
                InfectionMapper.toDTOList(city.getInfections())
        );
    }

    public static List<ICityDTO> toDTOList(List<City> cities) {
        List<ICityDTO> citiesDto = new ArrayList<>();
        for (City city : cities) {
            CityDTO cityDTO = new CityDTO(
                    city.getId(),
                    city.getPlagueName(),
                    city.getName(),
                    city.getFoundationDate(),
                    city.isHarbourCity(),
                    city.isHospitalBuilt(),
                    InfectionMapper.toDTOList(city.getInfections())
            );
            citiesDto.add(cityDTO);
        }
        return citiesDto;
    }
}