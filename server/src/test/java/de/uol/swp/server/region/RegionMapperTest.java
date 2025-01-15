package de.uol.swp.server.region;

import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.server.city.data.City;
import de.uol.swp.common.city.data.CityName;
import de.uol.swp.server.region.data.IRegion;
import de.uol.swp.server.region.data.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for the {@link RegionMapper} class.
 * Ensures the correct mapping of {@link Region} entities to their corresponding DTO representations.
 */
class RegionMapperTest {

    /**
     * Mocked instance of {@link Region} used for testing.
     */
    private Region mockRegion;

    /**
     * Mocked instance of {@link City} used as a secondary city in test scenarios.
     */
    private City mockSecondCity;

    /**
     * Sets up the test environment by creating and configuring mocked {@link Region} and {@link City} instances.
     */
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

    /**
     * Tests the {@link RegionMapper#toDTO(Region)} method to ensure proper mapping
     * of a {@link Region} entity to its corresponding DTO.
     */
    @Test
    void testToDTO() {
        IRegionDTO regionDTO = RegionMapper.toDTO(mockRegion);

        assertEquals(1, regionDTO.getId());
        assertEquals(
                Arrays.asList(CityName.ALBACETE.getDisplayName(), CityName.ZARAGOZA.getDisplayName()),
                regionDTO.getSurroundingCities()
        );
        assertEquals(3, regionDTO.getWaterTreatments());
        assertTrue(regionDTO.isPreventionMarker());
    }

    /**
     * Tests the {@link RegionMapper#toDTOList(List)} method to ensure correct mapping
     * of a list of {@link Region} entities to their corresponding DTOs.
     */
    @Test
    void testToDTOList() {
        Region mockRegion2 = mock(Region.class);
        when(mockRegion2.getId()).thenReturn(2);
        when(mockRegion2.getSurroundingCities()).thenReturn(List.of(mockSecondCity));
        when(mockRegion2.getWaterTreatments()).thenReturn(1);
        when(mockRegion2.isPreventionMarker()).thenReturn(false);

        List<IRegion> regions = Arrays.asList(mockRegion, mockRegion2);

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
