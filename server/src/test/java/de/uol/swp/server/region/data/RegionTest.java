package de.uol.swp.server.region.data;

import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.region.management.RegionManagementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RegionTest {

    private Region region;
    private ICity city1;
    private ICity city2;

    @BeforeEach
    void setUp() {
        city1 = mock(ICity.class);
        city2 = mock(ICity.class);
        region = new Region(1, List.of(city1, city2), 5, true);
    }

    @Test
    void testIncreaseWaterTreatments() {
        region.increaseWaterTreatments(3);
        assertEquals(8, region.getWaterTreatments());
    }

    @Test
    void testDecreaseWaterTreatments_Success() throws RegionManagementException {
        region.decreaseWaterTreatments(3);
        assertEquals(2, region.getWaterTreatments());
    }

    @Test
    void testDecreaseWaterTreatments_Exception() {
        Exception exception = assertThrows(RegionManagementException.class, () -> region.decreaseWaterTreatments(6));
        assertEquals("Nicht genug Wasseraufbereitungsmarker in dieser Region verfügbar", exception.getMessage());
    }

    @Test
    void testGetId() {
        assertEquals(1, region.getId());
    }

    @Test
    void testGetSurroundingCities() {
        assertEquals(List.of(city1, city2), region.getSurroundingCities());
    }

    @Test
    void testIsPreventionMarker() {
        assertTrue(region.isPreventionMarker());
    }

    @Test
    void testSetPreventionMarker() {
        region.setPreventionMarker(false);
        assertFalse(region.isPreventionMarker());
    }
}