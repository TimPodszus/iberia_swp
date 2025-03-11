package de.uol.swp.common.player.message.event;

import de.uol.swp.common.region.IRegionDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegionsForPreventionMarkerEventTest {

    @Test
    void testEqualsAndHashCode() {
        IRegionDTO region1 = mock(IRegionDTO.class);
        IRegionDTO region2 = mock(IRegionDTO.class);
        List<IRegionDTO> regions1 = List.of(region1, region2);
        List<IRegionDTO> regions2 = List.of(region1, region2);

        RegionsForPreventionMarkerEvent event1 = new RegionsForPreventionMarkerEvent("lobby1", regions1);
        RegionsForPreventionMarkerEvent event2 = new RegionsForPreventionMarkerEvent("lobby1", regions2);

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void testNotEquals() {
        IRegionDTO region1 = mock(IRegionDTO.class);
        IRegionDTO region2 = mock(IRegionDTO.class);
        IRegionDTO region3 = mock(IRegionDTO.class);
        List<IRegionDTO> regions1 = List.of(region1, region2);
        List<IRegionDTO> regions2 = List.of(region1, region3);

        RegionsForPreventionMarkerEvent event1 = new RegionsForPreventionMarkerEvent("lobby1", regions1);
        RegionsForPreventionMarkerEvent event2 = new RegionsForPreventionMarkerEvent("lobby1", regions2);

        assertNotEquals(event1, event2);
    }
}