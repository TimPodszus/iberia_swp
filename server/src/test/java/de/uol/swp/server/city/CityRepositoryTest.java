package de.uol.swp.server.city;

import de.uol.swp.common.city.CityName;
import de.uol.swp.server.city.data.ICity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CityRepositoryTest {
    static CityRepository repository;
    static List<ICity> cities;

    @BeforeAll
    static void create() {
        repository = new CityRepository();
        cities = repository.getCities();
    }

    @Test
    void testGetAllCities() {
        assertEquals(48, cities.size());
    }

    @Test
    void testGetCityByName() {
        List<ICity> city = repository.getCitiesByNames(CityName.MADRID);
        assertNotNull(city);
        assertEquals(
                CityName.MADRID,
                city.get(0)
                    .getName()
        );
    }
}