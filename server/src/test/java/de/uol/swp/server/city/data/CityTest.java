package de.uol.swp.server.city.data;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.infection.data.Infection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the City class.
 */
class CityTest {
    private ICity city;
    private ICity anotherCity;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        city = new City(1, PlagueName.MALARIA, CityName.A_CORUNA, 1000, true, false, new ArrayList<>());
        List<IInfection> infections = new ArrayList<>();
        infections.add(new Infection(5, PlagueName.MALARIA));
        city.setInfections(infections);
    }

    /**
     * Tests the City constructor and its getter methods.
     */
    @Test
    void testCityConstructorAndGetters() {
        assertEquals(1, city.getId());
        assertEquals(PlagueName.MALARIA, city.getPlagueName());
        assertEquals(CityName.A_CORUNA, city.getName());
        assertEquals(1000, city.getFoundationDate());
        assertTrue(city.isHarbourCity());
        assertFalse(city.isHospitalBuilt());
        assertNotNull(city.getInfections());
        assertFalse(city.getInfections()
                       .isEmpty());
    }

    /**
     * Tests the setHospitalBuilt method.
     */
    @Test
    void testSetHospitalBuilt() {
        city.setHospitalBuilt(true);
        assertTrue(city.isHospitalBuilt());
    }


    /**
     * Tests adding infections to the City.
     */
    @Test
    void testAddInfections() {
        IInfection infection = new Infection(1, PlagueName.MALARIA);

        city.getInfections()
            .add(infection);

        assertEquals(2,
                city.getInfections()
                    .size()
        );
        assertSame(infection,
                city.getInfections()
                    .get(1)
        );
    }
    /**
     * Tests the hash code.
     */
    @Test
    void testHashCode() {
        anotherCity = new City(1, PlagueName.MALARIA, CityName.A_CORUNA, 1000, true);
        assertEquals(city.hashCode(), anotherCity.hashCode());
    }

    /**
     * Tests if the city has a specific plague.
     */
    @Test
    void testHasPlague() {
        assertTrue(city.hasPlague(PlagueName.MALARIA));
        assertFalse(city.hasPlague(PlagueName.CHOLERA));
    }

    /**
     * Tests the number of plague cubes present in the city for a specific plague.
     */
    @Test
    void testGetPlagueCubes() {
        assertEquals(5, city.getPlagueCubes(PlagueName.MALARIA));
        assertEquals(0, city.getPlagueCubes(PlagueName.CHOLERA));
    }

    /**
     * Tests the removal of plague cubes from the city.
     */
    @Test
    void testRemovePlagueCubes() {
        city.removePlagueCubes(PlagueName.MALARIA, 3);
        assertEquals(2, city.getPlagueCubes(PlagueName.MALARIA));
    }

}