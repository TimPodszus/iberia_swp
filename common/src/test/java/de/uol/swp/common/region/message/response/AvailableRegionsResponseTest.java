package de.uol.swp.common.region.message.response;

import de.uol.swp.common.message.response.AbstractResponseMessage;
import de.uol.swp.common.region.IRegionDTO;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AvailableRegionsResponseTest {

    @Test
    void testEquals() {
        Set<IRegionDTO> regions1 = new HashSet<>();
        Set<IRegionDTO> regions2 = new HashSet<>();
        Set<IRegionDTO> regions3 = new HashSet<>();
        IRegionDTO region = mock(IRegionDTO.class);

        regions1.add(region);
        regions2.add(region);

        AvailableRegionsResponse response1 = new AvailableRegionsResponse(regions1);
        AvailableRegionsResponse response2 = new AvailableRegionsResponse(regions2);
        AvailableRegionsResponse response3 = new AvailableRegionsResponse(regions3);

        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
    }

    @Test
    void testHashCode() {
        Set<IRegionDTO> regions1 = new HashSet<>();
        Set<IRegionDTO> regions2 = new HashSet<>();
        Set<IRegionDTO> regions3 = new HashSet<>();
        IRegionDTO region = mock(IRegionDTO.class);

        regions1.add(region);
        regions2.add(region);

        AvailableRegionsResponse response1 = new AvailableRegionsResponse(regions1);
        AvailableRegionsResponse response2 = new AvailableRegionsResponse(regions2);
        AvailableRegionsResponse response3 = new AvailableRegionsResponse(regions3);

        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    void testConstructorAndGetRegions() {
        Set<IRegionDTO> regions = new HashSet<>();
        IRegionDTO region = mock(IRegionDTO.class);
        regions.add(region);

        AvailableRegionsResponse response = new AvailableRegionsResponse(regions);

        assertNotNull(response.getRegions());
        assertEquals(1, response.getRegions().size());
        assertTrue(response.getRegions().contains(region));
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        Set<IRegionDTO> regions = new HashSet<>();
        AvailableRegionsResponse response = new AvailableRegionsResponse(regions);
        assertNotEquals(null, response);
        assertNotEquals(new Object(), response);
    }

    @Test
    void testEqualsWithDifferentSubclass() {
        Set<IRegionDTO> regions = new HashSet<>();
        AvailableRegionsResponse response = new AvailableRegionsResponse(regions);
        AbstractResponseMessage differentSubclass = new AbstractResponseMessage() {};
        assertNotEquals(response, differentSubclass);
    }
}