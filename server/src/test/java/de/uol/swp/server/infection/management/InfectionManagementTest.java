package de.uol.swp.server.infection.management;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.infection.data.Infection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

class InfectionManagementTest {

    private static final String LOBBY_ID = "testLobbyId";

    @Mock
    IGame game;

    @Mock
    CityRepository cityRepository;

    @Mock
    ICity city;

    @InjectMocks
    InfectionManagement infectionManagement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        GameStore.getInstance()
                 .addGame(LOBBY_ID, game);
    }

    @Test
    void testFindInfection() {
        CityName cityName = CityName.PALMA_DE_MALLORCA;
        PlagueName plagueName = PlagueName.YELLOW_FEVER;

        when(game.getCityRepository()).thenReturn(cityRepository);
        when(cityRepository.getCityByName(cityName)).thenReturn(city);

        IInfection infection = new Infection(0, plagueName);
        when(city.getInfections()).thenReturn(List.of(infection));

        IInfection result = infectionManagement.findInfection(LOBBY_ID, cityName, plagueName);

        assertNotNull(result);
        assertEquals(infection, result);
    }
}