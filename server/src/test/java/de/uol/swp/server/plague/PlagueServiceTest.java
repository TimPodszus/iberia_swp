package de.uol.swp.server.plague;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.plague.PlagueResearchedMessage;
import de.uol.swp.common.plague.request.ResearchPlagueRequest;
import de.uol.swp.common.plague.request.TreatPlagueRequest;
import de.uol.swp.common.plague.response.TreatPlagueResponse;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.plague.management.IPlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.region.RegionRepository;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlagueServiceTest {

    private PlagueService plagueService;
    private IPlagueManagement plagueManagement;
    private EventBus eventBus;
    private Game game;
    private IPlayer player;
    private ICity city;
    private RegionRepository regionRepository;

    @BeforeEach
    public void setUp() {
        plagueManagement = mock(IPlagueManagement.class);
        eventBus = mock(EventBus.class);
        game = mock(Game.class);
        plagueService = new PlagueService(plagueManagement, eventBus);

        player = mock(IPlayer.class);
        city = mock(ICity.class);
        regionRepository = mock(RegionRepository.class);

        when(game.getCurrentPlayer()).thenReturn(player);
        when(player.getCurrentPosition()).thenReturn(city);
        when(game.getRegionRepository()).thenReturn(regionRepository);
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
        doThrow(new PlagueManagementException("Error")).when(plagueManagement).researchPlague(PlagueName.CHOLERA, game);

        assertThrows(PlagueManagementException.class, () -> plagueService.onResearchPlagueRequest(request, game));

        verify(plagueManagement, times(1)).researchPlague(PlagueName.CHOLERA, game);
        verify(eventBus, never()).post(any(PlagueResearchedMessage.class));
    }

    @Test
    void testOnTreatPlague() throws PlagueManagementException {
        String lobbyId = "lobbyId";

        TreatPlagueRequest request = new TreatPlagueRequest("lobby1", 123, PlagueName.CHOLERA);
        when(city.getId()).thenReturn(123);

        when(plagueManagement.getGame(lobbyId)).thenReturn(game);

        plagueService.onTreatPlagueRequest(request);

        verify(city).removePlagueCubes(PlagueName.CHOLERA, 1);

        ArgumentCaptor<TreatPlagueResponse> responseCaptor = ArgumentCaptor.forClass(TreatPlagueResponse.class);
        verify(eventBus).post(responseCaptor.capture());

        TreatPlagueResponse response = responseCaptor.getValue();
        assertEquals("lobby1", response.getLobbyId());
        assertTrue(response.isSuccess());
        assertEquals(PlagueName.CHOLERA, response.getPlagueName());
        assertEquals(123, response.getCityID());
    }
}
