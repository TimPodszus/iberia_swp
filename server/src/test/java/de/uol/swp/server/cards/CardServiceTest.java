package de.uol.swp.server.cards;

import de.uol.swp.common.cards.request.PlayCardRequest;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.cards.management.ICardManagement;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.game.GameException;
import de.uol.swp.server.game.data.Game;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test class for CardService.
 */
public class CardServiceTest extends EventBusBasedTest {

    @Mock
    private ICardManagement cardManagement;

    @Mock
    private ILobbyManagement lobbyManagement;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private CardService cardService = new CardService(getBus(), cardManagement, lobbyManagement);

    /**
     * Handles BoardUpdateEvent.
     *
     * @param event the BoardUpdateEvent to handle
     */
    @Subscribe
    public void onBoardUpdateEvent(BoardUpdateEvent event) {
        super.handleEvent(event);
    }

    /**
     * Sets up the test environment.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the onPlayCardRequest method with a valid session.
     *
     * @throws InterruptedException if the thread is interrupted
     */
    @Test
    void testOnPlayCardRequest() throws InterruptedException {
        IUser user = new User("testuser", "testpassword");
        Session session = UUIDSession.create(user);

        ILobby lobby = new Lobby("1234", "testLobby", new ArrayList<>(List.of(user)), user, 2);
        when(lobbyManagement.getLobby("1234")).thenReturn(lobby);

        IGame game = new Game(2, "1234");
        when(cardManagement.getGame("1234")).thenReturn(game);

        PlayCardRequest request = new PlayCardRequest("1234", 1);
        request.setSession(session);

        postAndWait(request);

        assertInstanceOf(BoardUpdateEvent.class, event, "Expected BoardUpdateEvent");
        verify(cardManagement, atLeastOnce()).playCard("1234", "testuser", 1);
    }

    /**
     * Tests the onPlayCardRequest method with a missing session.
     */
    @Test
    void testOnPlayCardRequestWithMissingSession() {
        PlayCardRequest request = new PlayCardRequest("1234", 1);
        assertThrows(GameException.class, () -> cardService.onPlayCardRequest(request));
    }
}
