package de.uol.swp.server.city;

import de.uol.swp.common.city.CityName;
import de.uol.swp.server.city.data.ICity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CityRepositoryTest {
    static CityRepository repository;

    @BeforeAll
    static void setUp() {
        repository = new CityRepository();
    }

    @Test
    void testRepositoryIsInitializedCorrectly() {
        assertNotNull(repository.getCities());
        assertFalse(repository.getCities().isEmpty());
    }

    @Test
    void testGetCityByName_Found() {
        ICity city = repository.getCityByName(CityName.MADRID);
        assertNotNull(city);
        assertEquals(CityName.MADRID, city.getName());
    }

    @Test
    void testGetCityByName_NotFound() {
        ICity city = repository.getCityByName(null);
        assertNull(city);
    }

    @Test
    void testGetCitiesByNames_SingleNameFound() {
        List<ICity> cities = repository.getCitiesByNames(CityName.MADRID);
        assertNotNull(cities);
        assertFalse(cities.isEmpty());
        assertEquals(1, cities.size());
        assertEquals(CityName.MADRID, cities.get(0).getName());
    }

    @Test
    void testGetCitiesByNames_MultipleNamesFound() {
        List<ICity> cities = repository.getCitiesByNames(CityName.MADRID, CityName.BARCELONA);
        assertNotNull(cities);
        assertEquals(2, cities.size());
    }

    @Test
    void testGetCitiesByNames_NullInput() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            repository.getCitiesByNames((CityName[]) null);
        });
        assertNotNull(exception);
    }

    @Test
    void testGetCityNameById_Found() {
        CityName cityName = repository.getCityNameById(1);
        assertNotNull(cityName);
        assertEquals(CityName.PORTO, cityName);
    }

    @Test
    void testGetCityNameById_NotFound() {
        CityName cityName = repository.getCityNameById(99);
        assertNull(cityName);
    }
}
