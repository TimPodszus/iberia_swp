package de.uol.swp.server.plague;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.plague.PlagueResearchedMessage;
import de.uol.swp.common.plague.ResearchPlagueRequest;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.plague.data.IPlague;
import de.uol.swp.server.plague.data.Plague;
import de.uol.swp.server.plague.data.PlagueRepository;
import de.uol.swp.server.plague.management.IPlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlagueServiceTest {

    private PlagueService plagueService;
    private IPlagueManagement plagueManagement;

    private PlagueRepository plagueRepository;
    private EventBus eventBus;
    private IGame game;

    @BeforeEach
    public void setUp() {
        plagueManagement = mock(IPlagueManagement.class);
        eventBus = mock(EventBus.class);
        game = mock(Game.class);
        plagueService = spy(new PlagueService(plagueManagement, eventBus));
        plagueRepository = mock(PlagueRepository.class);
        when(game.getPlagueRepository()).thenReturn(plagueRepository);
        eventBus.register(plagueService);

    }

    @Test
    void testOnResearchPlagueRequest() throws PlagueManagementException {
        String lobbyId = "123";
        PlagueName plagueName = PlagueName.CHOLERA;
        ResearchPlagueRequest request = new ResearchPlagueRequest(lobbyId);

        List<IPlague> plagueList = new ArrayList<>();
        plagueList.add(new Plague(plagueName, 5, false));
        when(game.getPlagueRepository().getPlagues()).thenReturn(plagueList);
        when(plagueManagement.getGame(lobbyId)).thenReturn(game);

        plagueService.onResearchPlagueRequest(request);

        verify(plagueManagement).researchPlague(game);

        PlagueResearchedMessage plagueResearchedMessage = new PlagueResearchedMessage(PlagueName.CHOLERA);
        verify(plagueService).sendToAll(plagueResearchedMessage);


        assert plagueResearchedMessage.getName().equals(plagueName);
    }

//    @Test
//    void testOnResearchPlagueRequest_Success() throws PlagueManagementException {
//        ResearchPlagueRequest request = new ResearchPlagueRequest(game.getGameId());
//        doNothing().when(plagueManagement).researchPlague(game);
//
//        plagueService.onResearchPlagueRequest(request);
//
//        verify(plagueManagement, times(1)).researchPlague(game);
//        verify(eventBus, times(1)).post(any(PlagueResearchedMessage.class));
//    }
//
//    @Test
//    void testOnResearchPlagueRequest_Exception() throws PlagueManagementException {
//        ResearchPlagueRequest request = new ResearchPlagueRequest(game.getGameId());
//        doThrow(new PlagueManagementException("Error")).when(plagueManagement).researchPlague(game);
//
//        assertThrows(PlagueManagementException.class, () -> plagueService.onResearchPlagueRequest(request));
//
//        verify(plagueManagement, times(1)).researchPlague(game);
//        verify(eventBus, never()).post(any(PlagueResearchedMessage.class));
//    }
}
