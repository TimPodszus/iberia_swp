package de.uol.swp.server.chat;

import com.google.inject.Inject;
import de.uol.swp.common.chat.PlayerChatMessage;
import de.uol.swp.common.chat.SendChatRequest;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagementException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChatService extends AbstractService {
    private final Chat chat;
    private final ILobbyManagement lobbyManagement;
    private static final Logger LOG = LogManager.getLogger(ChatService.class);

    @Inject
    public ChatService(Chat chat, EventBus bus, LobbyManagement lobbyManagement) {
        super(bus);
        this.chat = chat;
        this.lobbyManagement = lobbyManagement;
    }

    @Subscribe
    public void onSendChatRequest(SendChatRequest request) throws LobbyManagementException {
        LOG.info("Chat message received: {}", request.getMessage());
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());

        if (request.getLobbyId().isEmpty()) {
            throw new LobbyManagementException("Lobby not found for code: " + request.getLobbyId());
        }

        PlayerChatMessage playerChatMessage = new PlayerChatMessage(request.getLobbyId(), request.getMessage(), request.getSender());

        chat.addMessage(playerChatMessage);
        sendToAllInLobby(lobby, playerChatMessage);
    }
}
