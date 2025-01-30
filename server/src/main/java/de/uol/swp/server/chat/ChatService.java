package de.uol.swp.server.chat;

import de.uol.swp.common.chat.PlayerChatMessage;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import jakarta.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChatService extends AbstractService {
    private final Chat chat;
    private final ILobbyManagement lobbyManagement;

    @Inject
    public ChatService(Chat chat, EventBus bus, LobbyManagement lobbyManagement) {
        super(bus);
        this.chat = chat;
        this.lobbyManagement = lobbyManagement;
    }

    @Subscribe
    public void onChatRequest(PlayerChatMessage playerChatMessage) throws LobbyManagementException {
        ILobby lobby = lobbyManagement.getLobby(playerChatMessage.getLobbyCode());

        if (playerChatMessage.getLobbyCode().isEmpty()) {
            throw new LobbyManagementException("Lobby not found for code: " + playerChatMessage.getLobbyCode());
        }

        chat.addMessage(playerChatMessage);

        sendToAllInLobby(lobby, playerChatMessage);
    }
}
