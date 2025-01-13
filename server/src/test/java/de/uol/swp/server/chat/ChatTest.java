package de.uol.swp.server.chat;

import de.uol.swp.common.chat.ChatRequest;
import org.greenrobot.eventbus.EventBus;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ChatTest {

    @Test
    void handleOnChatRequest() {
        // Arrange
        ChatRequest chatRequest = new ChatRequest("lobby1", "player1", "Moin Leute");
        EventBus eventBus = new EventBus();
        ChatService chatService = mock(ChatService.class);
        ChatController chatController = new ChatController(chatService, eventBus);

        // Act
        eventBus.post(chatRequest);

        // Assert
        verify(chatService).onChatRequest(chatRequest);
    }
}
