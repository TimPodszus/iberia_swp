package de.uol.swp.server.infection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.infection.management.InfectionManagement;
import de.uol.swp.server.plague.data.IPlague;
import de.uol.swp.server.plague.data.PlagueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

class InfectionManagementTest {

    @Mock
    private ICity mockCity;
    @Mock
    private PlagueRepository mockPlagueRepository;
    @InjectMocks
    private InfectionManagement infectionManagement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindExistingInfection() {
        IInfection infection = mock(IInfection.class);

        when(infection.getPlagueName()).thenReturn(PlagueName.YELLOW_FEVER);
        List<IInfection> infections = new ArrayList<>();
        infections.add(infection);

        when(mockCity.getInfections()).thenReturn(infections);

        IInfection result = infectionManagement.findInfection(mockCity, PlagueName.YELLOW_FEVER);

        assertNotNull(result);
        assertEquals(infection, result);
    }

    @Test
    void testCreateNewInfection() throws Exception {
        List<IInfection> infections = new ArrayList<>();
        IPlague plague = mock(IPlague.class);

        when(mockCity.getInfections()).thenReturn(infections);
        when(mockPlagueRepository.getPlagueByName(PlagueName.YELLOW_FEVER)).thenReturn(plague);
        when(plague.getName()).thenReturn(PlagueName.YELLOW_FEVER);

        IInfection result = infectionManagement.findInfection(mockCity, PlagueName.YELLOW_FEVER);

        assertNotNull(result);
        assertTrue(infections.contains(result));
    }
}
