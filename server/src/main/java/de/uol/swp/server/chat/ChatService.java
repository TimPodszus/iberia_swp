package de.uol.swp.server.chat;

import de.uol.swp.common.chat.ChatRequest;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import jakarta.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Optional;

public class ChatService extends AbstractService {
    private final Chat chat;
    private final ILobbyManagement lobbyManagement;

    @Inject
    public ChatService(Chat chat, EventBus bus, LobbyManagement lobbyManagement) {
        super(bus);
        this.chat = chat;
        this.lobbyManagement = lobbyManagement;
    }

    /**
     * Fügt eine Nachricht zur Lobby hinzu und sendet sie an alle Spieler.
     *
     * @param chatRequest Die Chat-Nachricht
     */
    @Subscribe
    public void onChatRequest(ChatRequest chatRequest) throws LobbyManagementException {
        Optional<ILobby> optionalLobby = lobbyManagement.getLobby(chatRequest.getLobbyCode());

        if (optionalLobby.isEmpty()) {
            throw new LobbyManagementException("Lobby not found for code: " + chatRequest.getLobbyCode());
        }

        ILobby lobby = optionalLobby.get();

        chat.addMessage(chatRequest);

        sendToAllInLobby(lobby, chatRequest);
    }
}
