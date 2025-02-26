package de.uol.swp.server.plague;

import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.plague.data.PlagueRepository;
import de.uol.swp.server.plague.management.IPlagueManagement;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class PlagueServiceTest {

    private PlagueService plagueService;
    private IPlagueManagement plagueManagement;
    private ILobbyManagement lobbyManagement;
    private PlagueRepository plagueRepository;
    private EventBus eventBus;
    private IGame game;

    @BeforeEach
    public void setUp() {
        plagueManagement = mock(IPlagueManagement.class);
        eventBus = mock(EventBus.class);
        game = mock(Game.class);
        lobbyManagement = mock(ILobbyManagement.class);
        plagueService = spy(new PlagueService(plagueManagement, eventBus, lobbyManagement));
        plagueRepository = mock(PlagueRepository.class);
        when(game.getPlagueRepository()).thenReturn(plagueRepository);
        eventBus.register(plagueService);

    }

}
