package de.uol.swp.server.chat.management;

import de.uol.swp.common.chat.PlayerChatMessage;
import de.uol.swp.server.chat.ChatService;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import jakarta.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChatManagement {
    private final ChatService chatService;
    private final EventBus eventBus;

    @Inject
    public ChatManagement(ChatService chatService, EventBus eventBus) {
        this.chatService = chatService;
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

//    @Subscribe
//    public void handleChatRequest(PlayerChatMessage playerChatMessage) throws LobbyManagementException {
//        chatService.onChatRequest(playerChatMessage);
//    }

    public void shutdown() {
        eventBus.unregister(this);
    }
}
