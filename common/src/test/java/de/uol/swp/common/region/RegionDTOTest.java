package de.uol.swp.common.region;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegionDTOTest {
    @Test
    void testConstructor() {
        RegionDTO regionDTO = new RegionDTO(1, null, 0, false);
        assertEquals(1, regionDTO.getId());
        assertNull(regionDTO.getSurroundingCities());
        assertEquals(0, regionDTO.getWaterTreatments());
        assertFalse(regionDTO.isPreventionMarker());
    }
}
