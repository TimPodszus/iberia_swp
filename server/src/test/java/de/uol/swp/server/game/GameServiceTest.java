package de.uol.swp.server.game;

import de.uol.swp.common.game.message.request.AvailableActionsRequest;
import de.uol.swp.common.game.message.response.AvailableActionsResponse;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

public class GameServiceTest extends EventBusBasedTest {
    @Mock
    ILobbyManagement lobbyManagement;

    @Mock
    IGameManagement gameManagement;

    @InjectMocks
    GameService gameService = new GameService(super.getBus(), lobbyManagement, gameManagement);

    @Subscribe
    public void onEvent(AvailableActionsResponse event) {
        super.handleEvent(event);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testOnAvailableActionsRequest() throws InterruptedException {
        IUser user = new User("username", "password");
        Session session = UUIDSession.create(user);

        AvailableActionsRequest request = new AvailableActionsRequest("lobbyId");
        request.setSession(session);

        super.postAndWait(request);

        verify(gameManagement, atLeast(1)).getAvailableActions("lobbyId", user);
        assertInstanceOf(AvailableActionsResponse.class, event);
    }
}
