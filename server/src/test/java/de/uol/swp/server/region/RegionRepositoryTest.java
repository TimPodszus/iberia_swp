package de.uol.swp.server.region;

import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.region.data.IRegion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RegionRepositoryTest {
    static RegionRepository repository;
    static List<IRegion> regions;

    @BeforeAll
    static void create() {
        CityRepository cityRepository = new CityRepository();
        repository = new RegionRepository(cityRepository);
        regions = repository.getRegions();
    }

    @Test
    void testGetAllRegions() {
        assertEquals(34, regions.size());
    }

    @Test
    void testGetRegionByID() {
        IRegion region = repository.getRegionByID(1);
        assertNotNull(region);
    }
}