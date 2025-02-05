package de.uol.swp.server.region;

import de.uol.swp.common.city.CityName;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.region.data.IRegion;
import de.uol.swp.server.region.management.RegionManagement;
import de.uol.swp.server.region.management.RegionManagementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RegionManagementTest {
    @Mock
    private RegionManagement regionManagement;
    private IGame game;
    private ICity city;
    private IRegion region1;
    private IRegion region2;
    private RegionRepository regionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        game = mock(IGame.class);
        city = mock(ICity.class);
        region1 = mock(IRegion.class);
        region2 = mock(IRegion.class);
        regionRepository = mock(RegionRepository.class);

        when(game.getRegionRepository()).thenReturn(regionRepository);
    }

    @Test
     void testReduceWaterTreatments_Success() throws RegionManagementException {
        when(city.getName()).thenReturn(CityName.A_CORUNA);
        when(regionRepository.getRegionsByCityName(CityName.A_CORUNA)).thenReturn(List.of(region1, region2));
        when(region1.getWaterTreatments()).thenReturn(3);
        when(region2.getWaterTreatments()).thenReturn(2);

        int remaining = regionManagement.reduceWaterTreatments(game, city, 4);

        assertEquals(0, remaining);
        verify(region1, times(1)).decreaseWaterTreatments(3);
        verify(region2, times(1)).decreaseWaterTreatments(1);
    }

    @Test
     void testReduceWaterTreatments_PartialSuccess() throws RegionManagementException {
        when(city.getName()).thenReturn(CityName.A_CORUNA);
        when(regionRepository.getRegionsByCityName(CityName.A_CORUNA)).thenReturn(List.of(region1, region2));
        when(region1.getWaterTreatments()).thenReturn(1);
        when(region2.getWaterTreatments()).thenReturn(1);

        int remaining = regionManagement.reduceWaterTreatments(game, city, 3);

        assertEquals(1, remaining);
        verify(region1, times(1)).decreaseWaterTreatments(1);
        verify(region2, times(1)).decreaseWaterTreatments(1);
    }

    @Test
     void testReduceWaterTreatments_NoWaterTreatments() throws RegionManagementException {
        when(city.getName()).thenReturn(CityName.A_CORUNA);
        when(regionRepository.getRegionsByCityName(CityName.A_CORUNA)).thenReturn(List.of(region1, region2));
        when(region1.getWaterTreatments()).thenReturn(0);
        when(region2.getWaterTreatments()).thenReturn(0);

        int remaining = regionManagement.reduceWaterTreatments(game, city, 3);

        assertEquals(3, remaining);
        verify(region1, times(1)).decreaseWaterTreatments(0);
        verify(region2, times(1)).decreaseWaterTreatments(0);
    }
}