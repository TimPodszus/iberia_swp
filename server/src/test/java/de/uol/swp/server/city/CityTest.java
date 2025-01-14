package de.uol.swp.server.city;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.infection.IInfection;
import de.uol.swp.server.infection.Infection;
import de.uol.swp.server.plague.Plague;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the City class.
 */
class CityTest {
    private City city;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        city = new City(1, PlagueName.MALARIA, CityName.A_CORUNA, 1000, true, false, new ArrayList<>());
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
        assertTrue(city.getInfections().isEmpty());
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
     * Tests the default state of infections in the City.
     */
    @Test
    void testInfectionsDefaultState() {
        List<IInfection> infections = city.getInfections();

        assertNotNull(infections);
        assertTrue(infections.isEmpty());
    }

    /**
     * Tests adding infections to the City.
     */
    @Test
    void testAddInfections() {
        IInfection infection = new Infection(1, new Plague(PlagueName.MALARIA, 1, true));

        city.getInfections().add(infection);

        assertEquals(1, city.getInfections().size());
        assertSame(infection, city.getInfections().get(0));
    }
}