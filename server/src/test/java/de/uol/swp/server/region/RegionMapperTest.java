package de.uol.swp.server.region;

import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegionMapperTest {
    private Region mockRegion;
    private City mockSecondCity;

    @BeforeEach
    void setUp() {
        CityName firstCityName = CityName.ALBACETE;
        CityName secondCityName = CityName.ZARAGOZA;

        City mockFirstCity = mock(City.class);
        mockSecondCity = mock(City.class);

        when(mockFirstCity.getName()).thenReturn(firstCityName);
        when(mockSecondCity.getName()).thenReturn(secondCityName);

        mockRegion = mock(Region.class);
        when(mockRegion.getId()).thenReturn(1);
        when(mockRegion.getSurroundingCities()).thenReturn(Arrays.asList(mockFirstCity, mockSecondCity));
        when(mockRegion.getWaterTreatments()).thenReturn(3);
        when(mockRegion.isPreventionMarker()).thenReturn(true);
    }

    @Test
    void testToDTO_ValidRegion() {
        IRegionDTO regionDTO = RegionMapper.toDTO(mockRegion);

        assertEquals(1, regionDTO.getId());
        assertEquals(
                Arrays.asList(CityName.ALBACETE.getDisplayName(), CityName.ZARAGOZA.getDisplayName()),
                regionDTO.getSurroundingCities()
        );
        assertEquals(3, regionDTO.getWaterTreatments());
        assertTrue(regionDTO.isPreventionMarker());
    }

    @Test
    void testToDTOList_ValidRegions() {
        Region mockRegion2 = mock(Region.class);
        when(mockRegion2.getId()).thenReturn(2);
        when(mockRegion2.getSurroundingCities()).thenReturn(List.of(mockSecondCity));
        when(mockRegion2.getWaterTreatments()).thenReturn(1);
        when(mockRegion2.isPreventionMarker()).thenReturn(false);

        List<Region> regions = Arrays.asList(mockRegion, mockRegion2);

        List<IRegionDTO> regionDTOs = RegionMapper.toDTOList(regions);

        assertEquals(2, regionDTOs.size());

        IRegionDTO regionDTO1 = regionDTOs.get(0);
        assertEquals(1, regionDTO1.getId());
        assertEquals(
                Arrays.asList(CityName.ALBACETE.getDisplayName(), CityName.ZARAGOZA.getDisplayName()),
                regionDTO1.getSurroundingCities()
        );
        assertEquals(3, regionDTO1.getWaterTreatments());
        assertTrue(regionDTO1.isPreventionMarker());

        IRegionDTO regionDTO2 = regionDTOs.get(1);
        assertEquals(2, regionDTO2.getId());
        assertEquals(List.of(CityName.ZARAGOZA.getDisplayName()), regionDTO2.getSurroundingCities());
        assertEquals(1, regionDTO2.getWaterTreatments());
        assertFalse(regionDTO2.isPreventionMarker());
    }
}
