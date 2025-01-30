package de.uol.swp.server.player;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.player.request.DrawPlayerCardRequest;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.store.GameStore;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.usermanagement.UserMapper;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.mockito.Mockito.*;

class PlayerServiceTest {

    @Mock
    private EventBus eventBus;

    @Mock
    private IPlayerManagement playerManagement;

    private final IGame game = new Game(1, "validGameId");

    @Mock
    private DrawPlayerCardRequest request;

    @Mock
    private ICardDTO cardDTO;

    @Mock
    private Session session;

    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        playerService = new PlayerService(eventBus, playerManagement);
        IUser user = new User("testUser", "testPassword");

        when(request.getLobbyId()).thenReturn("validGameId");
        when(GameStore.getInstance().getGame(eq("validGameId"))).thenReturn(game);

        when(session.getUser()).thenReturn(UserMapper.toDTO(user));
        when(request.getSession()).thenReturn(Optional.of(session));
        GameStore.getInstance().addGame("validGameId", game); // Ensure the game is added to the store
    }

    @Test
    void onDrawPlayerCardRequest_Success() throws PlayerManagementException {
        when(playerManagement.drawPlayerCard(eq("validGameId"), any(IUser.class))).thenReturn(cardDTO);
        GameStore.getInstance().addGame("validGameId", game);

        playerService.onDrawPlayerCardRequest(request);

        verify(eventBus, times(2)).post(any());
    }

    @Test
    void onDrawPlayerCardRequest_PlayerManagementException() throws PlayerManagementException {
        doThrow(new PlayerManagementException("Error"))
                .when(playerManagement).drawPlayerCard(eq("validGameId"), any(IUser.class));

        playerService.onDrawPlayerCardRequest(request);

        verify(eventBus, times(2)).post(any());
    }
}