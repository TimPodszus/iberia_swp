package de.uol.swp.server.region;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.region.RegionDTO;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.region.data.IRegion;
import de.uol.swp.server.region.data.Region;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides utility methods to convert Region objects into their Data Transfer Object (DTO) forms.
 * This class facilitates the transformation of server-side region data, which includes cities and
 * prevention measures, into a format suitable for client-server communication.
 */
public class RegionMapper {

    // Private constructor to prevent instantiation
    private RegionMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Converts a Region object into a RegionDTO, encapsulating the region's data in a simpler,
     * transferable form. This includes converting the list of surrounding cities into their display names
     * and adding other region-specific details such as water treatment and prevention markers.
     *
     * @param region the Region object to be converted into a DTO
     * @return an IRegionDTO that represents the region with simplified data suitable for transmission
     */
    public static IRegionDTO toDTO(Region region) {
        return new RegionDTO(
                region.getId(),
                region.getSurroundingCities()
                      .stream()
                      .map(ICity::getName)
                      .map(CityName::getDisplayName)
                      .toList(),
                region.getWaterTreatments(),
                region.isPreventionMarker()
        );
    }

    /**
     * Converts a list of Region objects into a list of RegionDTOs. Each Region object in the list
     * is converted using the toDTO method and the results are collected into a list.
     * This method is particularly useful for batch processing of Region data for network transfer.
     *
     * @param regions the list of Region objects to convert
     * @return a List of IRegionDTO representing the converted regions
     */
    public static List<IRegionDTO> toDTOList(List<IRegion> regions) {
        List<IRegionDTO> regionDTOS = new ArrayList<>();
        for (IRegion region : regions) {
            RegionDTO regionDTO = new RegionDTO(
                    region.getId(),
                    region.getSurroundingCities()
                          .stream()
                          .map(ICity::getName)
                          .map(CityName::getDisplayName)
                          .toList(),
                    region.getWaterTreatments(),
                    region.isPreventionMarker()
            );
            regionDTOS.add(regionDTO);
        }
        return regionDTOS;
    }
}