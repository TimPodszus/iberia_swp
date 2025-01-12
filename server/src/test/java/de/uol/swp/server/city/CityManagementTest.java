package de.uol.swp.server.city;

import de.uol.swp.server.city.management.CityManagement;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.store.GameStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Test class for CityManagement.
 */
public class CityManagementTest {

    ICityManagement cityManagement;
    @Mock
    IGame game;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cityManagement = new CityManagement();
        GameStore.getInstance()
                 .addGame("lobbyCode", game);
    }

    /**
     * Tests the getCity method of CityManagement.
     */
    @Test
    void testGetCity() {
        when(game.getCityRepository()).thenReturn(new CityRepository());

        City city = cityManagement.getCity("lobbyCode", "1");

        assertEquals(1, city.getId());
    }
}
