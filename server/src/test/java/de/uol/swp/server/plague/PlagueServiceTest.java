package de.uol.swp.server.plague;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.plague.PlagueResearchedMessage;
import de.uol.swp.common.plague.ResearchPlagueRequest;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.plague.management.IPlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import de.uol.swp.server.player.management.PlayerManagementException;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
    void testOnResearchPlagueRequest_Success() throws PlagueManagementException, PlayerManagementException {
        ResearchPlagueRequest request = new ResearchPlagueRequest(PlagueName.CHOLERA);
        doNothing().when(plagueManagement)
                   .researchPlague(PlagueName.CHOLERA, game);

        plagueService.onResearchPlagueRequest(request, game);

        verify(plagueManagement, times(1)).researchPlague(PlagueName.CHOLERA, game);
        verify(eventBus, times(1)).post(any(PlagueResearchedMessage.class));
    }

    @Test
    void testOnResearchPlagueRequest_Exception() throws PlagueManagementException, PlayerManagementException {
        ResearchPlagueRequest request = new ResearchPlagueRequest(PlagueName.CHOLERA);
        doThrow(new PlagueManagementException("Error")).when(plagueManagement)
                                                       .researchPlague(PlagueName.CHOLERA, game);

        assertThrows(PlagueManagementException.class, () -> plagueService.onResearchPlagueRequest(request, game));

        verify(plagueManagement, times(1)).researchPlague(PlagueName.CHOLERA, game);
        verify(eventBus, never()).post(any(PlagueResearchedMessage.class));
    }
}
