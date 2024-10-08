package de.uol.swp.server.region;

import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.region.RegionDTO;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityName;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegionMapper {

    public static IRegionDTO toDTO(Region region) {
        return new RegionDTO(
                region.getId(),
                region.getSurroundingCities()
                      .stream()
                      .map(City::getName)
                      .map(CityName::getDisplayName)
                      .collect(Collectors.toList()),
                region.getWaterTreatments(),
                region.isPreventionMarker()
        );
    }

    public static List<IRegionDTO> toDTOList(List<Region> regions) {
        List<IRegionDTO> regionDTOS = new ArrayList<>();
        for (Region region : regions) {
            RegionDTO regionDTO = new RegionDTO(
                    region.getId(),
                    region.getSurroundingCities()
                          .stream()
                          .map(City::getName)
                          .map(CityName::getDisplayName)
                          .collect(Collectors.toList()),
                    region.getWaterTreatments(),
                    region.isPreventionMarker()
            );
            regionDTOS.add(regionDTO);
        }
        return regionDTOS;
    }
}
