package de.uol.swp.server.chat.management;

import de.uol.swp.server.chat.data.IChatMessage;
import de.uol.swp.server.chat.data.PlayerChatMessage;
import de.uol.swp.server.chat.data.ServerChatMessage;
import de.uol.swp.server.chat.store.IChatStore;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test class for ChatManagement.
 */
public class ChatManagementTest {

    @Mock
    private IChatStore chatStore;

    @Mock
    private ILobbyManagement lobbyManagement;

    @InjectMocks
    private ChatManagement chatManagement;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        chatManagement = new ChatManagement(chatStore, lobbyManagement);
    }

    /**
     * Tests the addChatMessage method with a ServerChatMessage.
     */
    @Test
    void testAddChatMessage() {
        IChatMessage chatMessage = new ServerChatMessage("lobbyId", "message");
        IChatMessage returnValue = chatManagement.addChatMessage(chatMessage.getLobbyId(), chatMessage.getMessage());

        assertEquals(chatMessage, returnValue);
        verify(chatStore, times(1)).addChatMessage(chatMessage);
    }

    /**
     * Tests the addChatMessage method with a PlayerChatMessage.
     */
    @Test
    void testAddChatMessageWithSender() {
        PlayerChatMessage chatMessage = new PlayerChatMessage("lobbyId", "sender", "message");
        IChatMessage returnValue = chatManagement.addChatMessage(chatMessage.getLobbyId(), chatMessage.getSender(), chatMessage.getMessage());

        assertEquals(chatMessage, returnValue);
        verify(chatStore, times(1)).addChatMessage(chatMessage);
    }

    /**
     * Tests the getChatMessages method for an authorized user.
     *
     * @throws IllegalAccessException if the user is not authorized to access the chat messages
     */
    @Test
    void testGetChatMessages() throws IllegalAccessException {
        IUser user = new User("username", "password");
        ILobby lobby = mock(ILobby.class);
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(lobby);
        when(lobby.getUsers()).thenReturn(List.of(user));

        IChatMessage chatMessage = new ServerChatMessage("lobbyId", "message");
        when(chatStore.getChatMessages("lobbyId")).thenReturn(List.of(chatMessage));

        List<IChatMessage> chatMessages = chatManagement.getChatMessages("lobbyId", user);

        assertEquals(List.of(chatMessage), chatMessages);
        verify(chatStore, times(1)).getChatMessages("lobbyId");
    }

    /**
     * Tests the getChatMessages method for an unauthorized user.
     */
    @Test
    void testGetChatMessages_Unauthorized() {
        IUser user = new User("username", "password");
        ILobby lobby = mock(ILobby.class);
        when(lobbyManagement.getLobby("lobbyId")).thenReturn(lobby);
        when(lobby.getUsers()).thenReturn(List.of());

        assertThrows(IllegalAccessException.class, () -> chatManagement.getChatMessages("lobbyId", user));
    }

    @Test
    void testRemoveChat() {
        chatManagement.removeChat("lobbyId");
        verify(chatStore, times(1)).removeChat("lobbyId");
    }
}