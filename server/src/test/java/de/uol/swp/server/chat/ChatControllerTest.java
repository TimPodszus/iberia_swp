package de.uol.swp.server.chat;

import de.uol.swp.common.chat.PlayerChatMessage;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ChatControllerTest {

    @Test
    void handleOnChatRequest() throws LobbyManagementException {
        // Arrange
        PlayerChatMessage playerChatMessage = new PlayerChatMessage("lobby1", "player1", "Moin Leute");
        EventBus eventBus = new EventBus();
        ChatService chatService = mock(ChatService.class);
        ChatController chatController = new ChatController(chatService, eventBus);

        // Act
        eventBus.post(playerChatMessage);

        // Assert
        verify(chatService).onChatRequest(playerChatMessage);
    }
}
