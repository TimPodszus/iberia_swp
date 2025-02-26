package de.uol.swp.server.chat;

import de.uol.swp.common.chat.messages.AbstractChatMessage;
import de.uol.swp.common.chat.request.GetChatRequest;
import de.uol.swp.common.chat.request.SendChatRequest;
import de.uol.swp.common.chat.response.GetChatResponse;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.EventBusBasedTest;
import de.uol.swp.server.chat.data.PlayerChatMessage;
import de.uol.swp.server.chat.event.ServerMessageEvent;
import de.uol.swp.server.chat.management.IChatManagement;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.data.Lobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;
import org.greenrobot.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Test class for ChatService.
 * Extends EventBusBasedTest to utilize event bus functionalities.
 */
public class ChatServiceTest extends EventBusBasedTest {
    @Mock
    private ILobbyManagement lobbyManagement;
    @Mock
    private IChatManagement chatManagement;
    @Mock
    AuthenticationService authenticationService;

    @InjectMocks
    private ChatService chatService = new ChatService(super.getBus(), lobbyManagement, chatManagement);

    private IUser user;
    private Session session;
    private ILobby lobby;

    /**
     * Handles AbstractChatMessage events.
     *
     * @param message the AbstractChatMessage event
     */
    @Subscribe
    public void onAbstractChatMessage(AbstractChatMessage message) {
        handleEvent(message);
    }

    /**
     * Handles GetChatResponse events.
     *
     * @param response the GetChatResponse event
     */
    @Subscribe
    public void onGetChatResponse(GetChatResponse response) {
        handleEvent(response);
    }

    /**
     * Handles StatusResponse events.
     *
     * @param response the StatusResponse event
     */
    @Subscribe
    public void onStatusResponse(StatusResponse response) {
        handleEvent(response);
    }

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User("user", "password");
        session = UUIDSession.create(user);
        lobby = new Lobby("lobbyId", "lobbyName", List.of(user), user, 2);
        when(authenticationService.getSessions(Set.of(user))).thenReturn(List.of(session));
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(lobby);
    }

    /**
     * Tests the handling of SendChatRequest.
     *
     * @throws InterruptedException if the thread is interrupted
     */
    @Test
    void testOnSendChatRequest() throws InterruptedException {
        SendChatRequest request = new SendChatRequest("lobbyId", "message");
        request.setSession(session);

        PlayerChatMessage playerChatMessage = new PlayerChatMessage("lobbyId", user.getUsername(), "message");
        when(chatManagement.addChatMessage("lobbyId", user.getUsername(), "message")).thenReturn(playerChatMessage);

        postAndWait(request);

        assertInstanceOf(de.uol.swp.common.chat.messages.PlayerChatMessage.class, event);
        assertEquals(playerChatMessage.getLobbyId(), ((de.uol.swp.common.chat.messages.PlayerChatMessage) event).getLobbyId());
        assertEquals(playerChatMessage.getSender(), ((de.uol.swp.common.chat.messages.PlayerChatMessage) event).getSender());
        assertEquals(playerChatMessage.getMessage(), ((de.uol.swp.common.chat.messages.PlayerChatMessage) event).getMessage());
    }

    /**
     * Tests the handling of SendChatRequest without a session.
     */
    @Test
    void testOnSendChatRequest_WithoutSession() {
        SendChatRequest request = new SendChatRequest("lobbyId", "message");

        assertThrows(SessionNotFoundException.class, () -> chatService.onSendChatRequest(request));
    }

    /**
     * Tests the handling of GetChatRequest.
     *
     * @throws IllegalAccessException if access is illegal
     * @throws InterruptedException if the thread is interrupted
     */
    @Test
    void onGetChatRequest() throws IllegalAccessException, InterruptedException {
        GetChatRequest request = new GetChatRequest("lobbyId");
        request.setSession(session);
        PlayerChatMessage playerChatMessage = new PlayerChatMessage("lobbyId", user.getUsername(), "message");
        when(chatManagement.getChatMessages("lobbyId", user)).thenReturn(List.of(playerChatMessage));

        postAndWait(request);

        assertInstanceOf(GetChatResponse.class, event);
        assertEquals("lobbyId", ((GetChatResponse) event).getLobbyId());
        assertEquals(1, ((GetChatResponse) event).getChatMessages().size());
    }

    /**
     * Tests the handling of GetChatRequest without a session.
     */
    @Test
    void testOnGetChatRequest_WithoutSession() {
        GetChatRequest request = new GetChatRequest("lobbyId");

        assertThrows(SessionNotFoundException.class, () -> chatService.onGetChatRequest(request));
    }

    /**
     * Tests the handling of GetChatRequest with failed authorization.
     *
     * @throws IllegalAccessException if access is illegal
     * @throws InterruptedException if the thread is interrupted
     */
    @Test
    void testOnGetChatRequest_FailedAuthorization() throws IllegalAccessException, InterruptedException {
        GetChatRequest request = new GetChatRequest("lobbyId");
        request.setSession(session);
        when(chatManagement.getChatMessages("lobbyId", user)).thenThrow(new IllegalAccessException());

        postAndWait(request);

        assertInstanceOf(StatusResponse.class, event);
        assertEquals("lobbyId", ((StatusResponse) event).getLobbyId());
        assertFalse(((StatusResponse) event).isSuccess());
    }

    /**
     * Tests the handling of ServerMessageEvent.
     *
     * @throws InterruptedException if the thread is interrupted
     */
    @Test
    void testOnServerMessageEvent() throws InterruptedException {
        ServerMessageEvent serverMessageEvent = new ServerMessageEvent("lobbyId", "message");

        when(chatManagement.addChatMessage("lobbyId", "message")).thenReturn(new de.uol.swp.server.chat.data.ServerChatMessage("lobbyId", "message"));

        postAndWait(serverMessageEvent);

        assertInstanceOf(de.uol.swp.common.chat.messages.ServerChatMessage.class, event);
        assertEquals("lobbyId", ((de.uol.swp.common.chat.messages.ServerChatMessage) event).getLobbyId());
        assertEquals("message", ((de.uol.swp.common.chat.messages.ServerChatMessage) event).getMessage());
    }
}
