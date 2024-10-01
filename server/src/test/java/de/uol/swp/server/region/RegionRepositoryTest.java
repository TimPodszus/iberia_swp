package de.uol.swp.server.region;

import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.region.Region;
import de.uol.swp.server.region.RegionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RegionRepositoryTest {
    static RegionRepository repository;
    static List<Region> regions;

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
        Region region = repository.getRegionByID(1);
        assertNotNull(region);
    }
}