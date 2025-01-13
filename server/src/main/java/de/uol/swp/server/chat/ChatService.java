package de.uol.swp.server.chat;

import de.uol.swp.common.chat.AbstractChatMessage;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.lobby.management.LobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import jakarta.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChatService extends AbstractService {
    private final Chat chat;
    private final LobbyManagement lobbyManagement;

    @Inject
    public ChatService(Chat chat, EventBus bus, LobbyManagement lobbyManagement) {
        super(bus);
        this.chat = chat;
        this.lobbyManagement = lobbyManagement;
    }

    /**
     * Fügt eine Nachricht zur Lobby hinzu und sendet sie an alle Spieler.
     *
     * @param chatMessage Die Chat-Nachricht
     */
    @Subscribe
    public void onChatRequest(AbstractChatMessage chatMessage) {
        chat.addMessage(chatMessage);
        try {
            sendToAllInLobby(chatMessage.getLobbyCode(), chatMessage);
        } catch (LobbyManagementException e) {
            e.printStackTrace();
        }
    }
}
