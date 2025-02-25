package de.uol.swp.server.plague;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.RoleEnum;
import de.uol.swp.common.plague.PlagueResearchedMessage;
import de.uol.swp.common.plague.request.ResearchPlagueRequest;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.plague.management.IPlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.region.RegionRepository;
import de.uol.swp.server.role.IRole;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlagueServiceTest {

    @InjectMocks
    private PlagueService plagueService;
    @Mock
    private IPlagueManagement plagueManagement;
    @Mock
    private EventBus eventBus;
    @Mock
    private Game game;
    @Mock
    private IPlayer player;
    @Mock
    private ICity city;
    @Mock
    private RegionRepository regionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(game.getCurrentPlayer()).thenReturn(player);
        when(player.getCurrentPosition()).thenReturn(city);
        when(game.getRegionRepository()).thenReturn(regionRepository);

        IRole roleMock = mock(IRole.class);
        when(player.getRole()).thenReturn(roleMock);
        when(roleMock.getName()).thenReturn(RoleEnum.COUNTRY_DOCTOR);
        when(plagueManagement.getGame(anyString())).thenReturn(game);
    }

    @Test
    void testOnResearchPlagueRequest_Success() throws PlagueManagementException {
        ResearchPlagueRequest request = new ResearchPlagueRequest(PlagueName.CHOLERA);
        doNothing().when(plagueManagement).researchPlague(PlagueName.CHOLERA, game);

        plagueService.onResearchPlagueRequest(request, game);

        verify(plagueManagement, times(1)).researchPlague(PlagueName.CHOLERA, game);
        verify(eventBus, times(1)).post(any(PlagueResearchedMessage.class));
    }

    @Test
    void testOnResearchPlagueRequest_Exception() throws PlagueManagementException {
        ResearchPlagueRequest request = new ResearchPlagueRequest(PlagueName.CHOLERA);
        doThrow(new PlagueManagementException("Error")).when(plagueManagement)
                                                       .researchPlague(PlagueName.CHOLERA, game);

        assertThrows(PlagueManagementException.class, () -> plagueService.onResearchPlagueRequest(request, game));

        verify(plagueManagement, times(1)).researchPlague(PlagueName.CHOLERA, game);
        verify(eventBus, never()).post(any(PlagueResearchedMessage.class));
    }
}
