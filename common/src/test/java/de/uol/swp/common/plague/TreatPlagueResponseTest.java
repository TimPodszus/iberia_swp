package de.uol.swp.common.plague;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.city.CityName;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.plague.response.TreatPlagueResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreatPlagueResponseTest {

    @Test
    void testConstructorAndGetters() {
        List<ICityDTO> cities = new ArrayList<>();
        PlagueName plague = PlagueName.CHOLERA;
        CityName cityName = CityName.ALBACETE;
        CityDTO cityDTO = new CityDTO(1, plague, cityName, 1624, true, false, new ArrayList<>());
        cities.add(cityDTO);

        TreatPlagueResponse response = new TreatPlagueResponse("lobby123", true, cities, true);

        assertEquals("lobby123", response.getLobbyId());
        assertTrue(response.isSuccess());
        assertTrue(response.isCountryDoctor());
        assertEquals(cities, response.getAvailableCities());
    }

    @Test
    void testEquals() {
        List<ICityDTO> cities1 = new ArrayList<>();
        CityDTO city1 = new CityDTO(1, PlagueName.CHOLERA, CityName.A_CORUNA, 1624, true, false, new ArrayList<>());
        cities1.add(city1);

        List<ICityDTO> cities2 = new ArrayList<>();
        CityDTO city2 = new CityDTO(2, PlagueName.MALARIA, CityName.ALBACETE, 1500, false, true, new ArrayList<>());
        cities2.add(city2);

        TreatPlagueResponse response1 = new TreatPlagueResponse("lobby123", true, cities1, true);
        TreatPlagueResponse response2 = new TreatPlagueResponse("lobby123", true, cities1, true);
        TreatPlagueResponse response3 = new TreatPlagueResponse("lobby123", true, cities2, true);
        TreatPlagueResponse response4 = new TreatPlagueResponse("lobby456", true, cities1, true);
        TreatPlagueResponse response5 = new TreatPlagueResponse("lobby123", false, cities1, true);

        assertEquals(response1, response1);
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertNotEquals(response1, response4);
        assertNotEquals(response1, response5);
        assertNotEquals(response1, null);
        assertNotEquals(response1, new Object());
    }

    @Test
    void testHashCode() {
        List<ICityDTO> cities = new ArrayList<>();
        CityDTO city1 = new CityDTO(1, PlagueName.CHOLERA, CityName.A_CORUNA, 12, true, false, new ArrayList<>());
        cities.add(city1);

        TreatPlagueResponse response1 = new TreatPlagueResponse("lobby123", true, cities, true);
        TreatPlagueResponse response2 = new TreatPlagueResponse("lobby123", true, cities, true);
        TreatPlagueResponse response3 = new TreatPlagueResponse("lobby456", true, cities, true);

        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }
}

