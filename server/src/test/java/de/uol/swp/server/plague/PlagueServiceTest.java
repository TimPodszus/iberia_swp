package de.uol.swp.server.plague;

import de.uol.swp.common.plague.PlagueResearchedMessage;
import de.uol.swp.common.plague.ResearchPlagueRequest;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.plague.management.IPlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PlagueServiceTest {

    private PlagueService plagueService;
    private IPlagueManagement plagueManagement;
    private EventBus eventBus;
    private Game game;

    @BeforeEach
    public void setUp() {
        plagueManagement = mock(IPlagueManagement.class);
        eventBus = mock(EventBus.class);
        game = mock(Game.class);
        plagueService = new PlagueService(plagueManagement, eventBus);
    }

    @Test
    void testOnResearchPlagueRequest_Success() throws PlagueManagementException {
        ResearchPlagueRequest request = new ResearchPlagueRequest(game.getGameId());
        doNothing().when(plagueManagement).researchPlague(game);

        plagueService.onResearchPlagueRequest(request);

        verify(plagueManagement, times(1)).researchPlague(game);
        verify(eventBus, times(1)).post(any(PlagueResearchedMessage.class));
    }

    @Test
    void testOnResearchPlagueRequest_Exception() throws PlagueManagementException {
        ResearchPlagueRequest request = new ResearchPlagueRequest(game.getGameId());
        doThrow(new PlagueManagementException("Error")).when(plagueManagement).researchPlague(game);

        assertThrows(PlagueManagementException.class, () -> plagueService.onResearchPlagueRequest(request));

        verify(plagueManagement, times(1)).researchPlague(game);
        verify(eventBus, never()).post(any(PlagueResearchedMessage.class));
    }
}
