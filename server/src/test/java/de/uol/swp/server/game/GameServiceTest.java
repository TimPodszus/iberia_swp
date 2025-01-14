package de.uol.swp.server.game;

import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.player.request.MovePlayerRequest;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.game.management.GameManagement;
import de.uol.swp.server.game.management.GameManagementException;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.usermanagement.UserMapper;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

public class GameServiceTest extends EventBusBasedTest {

    @Mock
    private IGameManagement gameManagement;
    @Mock
    private ICityManagement cityManagement;

    @Subscribe
    public void onEvent(BoardUpdateEvent e) {
        handleEvent(e);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOnMovePlayerRequest() throws InterruptedException, GameManagementException {
        IUserDTO userDTO = new UserDTO("Marco", "Marco");
        MovePlayerRequest movePlayerRequest = new MovePlayerRequest("lobbycode", "12");
        City city = new CityRepository().getCity("12");
        when(cityManagement.getCity("lobbycode", "12")).thenReturn(city);

        postAndWait(movePlayerRequest);

        verify(gameManagement, atLeast(1)).movePlayer(UserMapper.toUser(userDTO), "lobbyCode", city);
        assertInstanceOf(BoardUpdateEvent.class, event);
    }
}
