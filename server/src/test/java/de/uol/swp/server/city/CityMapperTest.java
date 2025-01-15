package de.uol.swp.server.city;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.infection.IInfection;
import de.uol.swp.server.city.data.City;
import de.uol.swp.server.city.data.ICity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for the CityMapper utility class.
 * This class verifies the functionality of mapping {@link City} objects to {@link ICityDTO} objects.
 */
class CityMapperTest {
    /**
     * Mocked instance of the first {@link City} object used for testing.
     */
    @Mock
    private City firstCity;

    /**
     * Mocked instance of the second {@link City} object used for testing.
     */
    @Mock
    private City secondCity;

    /**
     * Sets up test objects before each test method.
     * Initializes the {@link City} instances with predefined values.
     */
    @BeforeEach
    public void setUp() {
        List<IInfection> infectionsFirstCity = new ArrayList<>();
        List<IInfection> infectionsSecondCity = new ArrayList<>();

        firstCity = new City(1, PlagueName.CHOLERA, CityName.A_CORUNA, 12, false, false, infectionsFirstCity);
        secondCity = new City(1, PlagueName.CHOLERA, CityName.LISBOA, 3, false, false, infectionsSecondCity);
    }

    /**
     * Tests the {@link CityMapper#toDTO(ICity)} method.
     * Verifies that a {@link City} object is correctly mapped to an {@link ICityDTO} object.
     */
    @Test
    void testToDTO() {
        ICityDTO cityDTO = CityMapper.toDTO(firstCity);

        assertNotNull(cityDTO);
        assertEquals(
                firstCity.getName()
                         , cityDTO.getName()
        );
        assertEquals(firstCity.getFoundationDate(), cityDTO.getFoundationDate());
        assertEquals(
                firstCity.getPlagueName()
                         , cityDTO.getPlagueName()
        );
        assertEquals(firstCity.isHarbourCity(), cityDTO.isHarbourCity());
        assertEquals(firstCity.isHospitalBuilt(), cityDTO.isHospitalBuild());
    }

    /**
     * Tests the {@link CityMapper#toDTOList(List)} method.
     * Verifies that a list of {@link City} objects is correctly mapped to a list of {@link ICityDTO} objects.
     */
    @Test
    void testToDTOList() {
        List<ICity> cities = Arrays.asList(firstCity, secondCity);

        List<ICityDTO> cityDTOList = CityMapper.toDTOList(cities);

        assertNotNull(cityDTOList);
        assertEquals(2, cityDTOList.size());

        ICityDTO firstCityDTO = cityDTOList.get(0);
        assertEquals(
                firstCity.getPlagueName()
                         , firstCityDTO.getPlagueName()
        );
        assertEquals(
                firstCity.getName()
                         , firstCityDTO.getName()
        );
        assertEquals(firstCity.getFoundationDate(), firstCityDTO.getFoundationDate());

        ICityDTO secondCityDTO = cityDTOList.get(1);
        assertEquals(
                secondCity.getPlagueName()
                          , secondCityDTO.getPlagueName()
        );
        assertEquals(
                secondCity.getName()
                          , secondCityDTO.getName()
        );
        assertEquals(secondCity.getFoundationDate(), secondCityDTO.getFoundationDate());
    }
}
