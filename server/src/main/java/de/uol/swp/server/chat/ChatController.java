package de.uol.swp.server.chat;

import de.uol.swp.common.chat.PlayerChatMessage;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import jakarta.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChatController {
    private final ChatService chatService;
    private final EventBus eventBus;

    @Inject
    public ChatController(ChatService chatService, EventBus eventBus) {
        this.chatService = chatService;
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    /**
     * Empfängt eine Chat-Nachricht von einem Spieler und verteilt sie an die Lobby.
     *
     * @param playerChatMessage Die empfangene Nachricht
     */
    @Subscribe
    public void handleChatRequest(PlayerChatMessage playerChatMessage) throws LobbyManagementException {
        chatService.onChatRequest(playerChatMessage);
    }

    /**
     * Deregistriert den Controller vom EventBus, z. B. bei der Abschaltung.
     */
    public void shutdown() {
        eventBus.unregister(this);
    }
}
