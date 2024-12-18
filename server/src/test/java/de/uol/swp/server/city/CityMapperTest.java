package de.uol.swp.server.city;

import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.PlagueName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CityMapperTest {
    @Mock
    private City firstCity;
    @Mock
    private City secondCity;
    @BeforeEach
    public void setUp() {
        firstCity = new City(1, PlagueName.CHOLERA, CityName.A_CORUNA, 12, false, false);
        secondCity = new City(1, PlagueName.CHOLERA, CityName.LISBOA, 3, false, false);
    }

    @Test
    void testToDTO() {
        ICityDTO cityDTO = CityMapper.toDTO(firstCity);

        assertNotNull(cityDTO);
        assertEquals(firstCity.getName().toString(), cityDTO.getName());
        assertEquals(firstCity.getFoundationDate(), cityDTO.getFoundationDate());
        assertEquals(firstCity.getPlagueName().toString(), cityDTO.getPlagueName());
        assertEquals(firstCity.isHarbourCity(), cityDTO.isHarbourCity());
        assertEquals(firstCity.isHospitalBuilt(), cityDTO.isHospitalBuild());
    }

    @Test
    void testToDTOList() {
        List<City> cities = Arrays.asList(firstCity, secondCity);

        List<ICityDTO> cityDTOList = CityMapper.toDTOList(cities);

        assertNotNull(cityDTOList);
        assertEquals(2, cityDTOList.size());

        ICityDTO firstCityDTO = cityDTOList.get(0);
        assertEquals(firstCity.getPlagueName().toString(), firstCityDTO.getPlagueName());
        assertEquals(firstCity.getName().toString(), firstCityDTO.getName());
        assertEquals(firstCity.getFoundationDate(), firstCityDTO.getFoundationDate());

        ICityDTO secondCityDTO = cityDTOList.get(1);
        assertEquals(secondCity.getPlagueName().toString(), secondCityDTO.getPlagueName());
        assertEquals(secondCity.getName().toString(), secondCityDTO.getName());
        assertEquals(secondCity.getFoundationDate(), secondCityDTO.getFoundationDate());
    }
}
