package de.uol.swp.common.city;

import de.uol.swp.common.game.PlagueName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CityDTOTest {
    @Test
    void testCityDTOGetters() {
        CityDTO cityDTO = new CityDTO(
                1,
                PlagueName.MALARIA,
                CityName.A_CORUNA,
                1000,
                true,
                false,
                Collections.emptyList()
        );

        assertEquals(1, cityDTO.getId());
        assertEquals(PlagueName.MALARIA, cityDTO.getPlagueName());
        assertEquals(CityName.A_CORUNA, cityDTO.getName());
        assertEquals(1000, cityDTO.getFoundationDate());
        assertTrue(cityDTO.isHarbourCity());
        assertFalse(cityDTO.isHospitalBuild());
        assertEquals(Collections.emptyList(), cityDTO.getInfections());
    }

    @Test
    void testEqualsAndHashCode() {
        CityDTO city1 = new CityDTO(
                1,
                PlagueName.MALARIA,
                CityName.A_CORUNA,
                1000,
                true,
                false,
                Collections.emptyList()
        );

        CityDTO city2 = new CityDTO(
                1,
                PlagueName.MALARIA,
                CityName.A_CORUNA,
                1000,
                true,
                false,
                Collections.emptyList()
        );

        CityDTO city3 = new CityDTO(
                2,
                PlagueName.CHOLERA,
                CityName.A_CORUNA,
                1200,
                false,
                true,
                Collections.emptyList()
        );

        CityDTO city4 = null;

        assertEquals(city1, city1);
        assertNotEquals("test", city1);
        assertEquals(city1, city2);
        assertNotEquals(city1, city3);
        assertEquals(city1.hashCode(), city2.hashCode());
        assertNotEquals(city1.hashCode(), city3.hashCode());
        assertNotEquals(city1, null);
    }
}
