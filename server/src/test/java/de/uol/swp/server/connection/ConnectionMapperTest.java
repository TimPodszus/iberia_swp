package de.uol.swp.server.connection;

import de.uol.swp.common.connectiom.IConnectionDTO;
import de.uol.swp.common.city.CityName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the ConnectionMapper utility class.
 * This class verifies the functionality of mapping {@link Connection} objects to {@link IConnectionDTO} objects.
 */
class ConnectionMapperTest {
    /**
     * Instance of the first {@link Connection} object used for testing.
     */
    private Connection firstCityConnection;

    /**
     * List of {@link Connection} objects used in tests.
     */
    private List<Connection> connectionList;

    /**
     * Sets up test objects before each test method.
     * Initializes {@link Connection} instances with predefined values and adds them to a list.
     */
    @BeforeEach
    void setUp() {
        firstCityConnection = new Connection(1, Arrays.asList(CityName.BARCELONA, CityName.ALICANTE), true, true);
        Connection secondCityConnection = new Connection(2, Arrays.asList(CityName.ZARAGOZA, CityName.GIRONA), false, false);

        connectionList = Arrays.asList(firstCityConnection, secondCityConnection);
    }

    /**
     * Tests the {@link ConnectionMapper#toDTO(Connection)} method.
     * Verifies that a {@link Connection} object is correctly mapped to an {@link IConnectionDTO} object.
     */
    @Test
    void testToDTO() {
        IConnectionDTO connectionDTO = ConnectionMapper.toDTO(firstCityConnection);

        assertNotNull(connectionDTO);
        assertEquals(1, connectionDTO.getId());
        assertEquals(2, connectionDTO.getCityNames().size());
        assertTrue(connectionDTO.getCityNames().contains(CityName.BARCELONA.getDisplayName()));
        assertTrue(connectionDTO.getCityNames().contains(CityName.ALICANTE.getDisplayName()));
        assertTrue(connectionDTO.isTrainTrack());
        assertTrue(connectionDTO.isTrainTrackBuildable());
    }

    /**
     * Tests the {@link ConnectionMapper#toDTOList(List)} method.
     * Verifies that a list of {@link Connection} objects is correctly mapped to a list of {@link IConnectionDTO} objects.
     */
    @Test
    void testToDTOList() {
        List<IConnectionDTO> connectionDTOS = ConnectionMapper.toDTOList(connectionList);

        assertNotNull(connectionDTOS);
        assertEquals(2, connectionDTOS.size());

        IConnectionDTO dto1 = connectionDTOS.get(0);
        assertEquals(1, dto1.getId());
        assertTrue(dto1.getCityNames().contains(CityName.BARCELONA.getDisplayName()));
        assertTrue(dto1.getCityNames().contains(CityName.ALICANTE.getDisplayName()));

        IConnectionDTO dto2 = connectionDTOS.get(1);
        assertEquals(2, dto2.getId());
        assertTrue(dto2.getCityNames().contains(CityName.ZARAGOZA.getDisplayName()));
        assertTrue(dto2.getCityNames().contains(CityName.GIRONA.getDisplayName()));
    }
}
