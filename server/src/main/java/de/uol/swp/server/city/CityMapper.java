package de.uol.swp.server.city;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.server.city.data.ICity;

import java.util.ArrayList;
import java.util.List;

public class CityMapper {

    // Private constructor to hide the implicit public one
    private CityMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ICityDTO toDTO(ICity city) {
        return new CityDTO(
                city.getPlagueName()
                    .toString(),
                city.getName()
                    .toString(),
                city.getFoundationDate(),
                city.isHarbourCity(),
                city.isHospitalBuilt()
        );
    }

    public static List<ICityDTO> toDTOList(List<ICity> cities) {
        List<ICityDTO> citiesDto = new ArrayList<>();
        for (ICity city : cities) {
            CityDTO cityDTO = new CityDTO(
                    city.getPlagueName()
                        .toString(),
                    city.getName()
                        .toString(),
                    city.getFoundationDate(),
                    city.isHarbourCity(),
                    city.isHospitalBuilt()
            );
            citiesDto.add(cityDTO);
        }
        return citiesDto;
    }
}