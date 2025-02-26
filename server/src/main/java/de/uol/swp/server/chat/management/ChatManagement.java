package de.uol.swp.server.chat.management;

import com.google.inject.Inject;
import de.uol.swp.server.chat.data.IChatMessage;
import de.uol.swp.server.chat.data.PlayerChatMessage;
import de.uol.swp.server.chat.data.ServerChatMessage;
import de.uol.swp.server.chat.store.IChatStore;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.usermanagement.IUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ChatManagement implements IChatManagement {

    private static final Logger LOG = LogManager.getLogger(ChatManagement.class);

    private final IChatStore chatStore;

    private final ILobbyManagement lobbyManagement;

    @Inject
    public ChatManagement(IChatStore chatStore, ILobbyManagement lobbyManagement) {
        this.chatStore = chatStore;
        this.lobbyManagement = lobbyManagement;
    }

    @Override
    public IChatMessage addChatMessage(String lobbyId, String message) {
        LOG.debug("[LobbyId: {}] Adding chat message from server", lobbyId);
        IChatMessage chatMessage = new ServerChatMessage(lobbyId, message);
        chatStore.addChatMessage(chatMessage);
        LOG.info("[LobbyId: {}] Chat message from server added", lobbyId);
        return chatMessage;
    }

    @Override
    public IChatMessage addChatMessage(String lobbyId, String sender, String message) {
        LOG.debug("[LobbyId: {}] Adding chat message from player {}", lobbyId, sender);
        IChatMessage chatMessage = new PlayerChatMessage(lobbyId, sender, message);
        chatStore.addChatMessage(chatMessage);
        LOG.info("[LobbyId: {}] Chat message from player added", lobbyId);
        return chatMessage;
    }

    @Override
    public List<IChatMessage> getChatMessages(String lobbyId, IUser user) throws IllegalAccessException {
        LOG.debug("[LobbyId: {}] Getting chat messages", lobbyId);
        if (!lobbyManagement.getLobby(lobbyId)
                            .getUsers()
                            .contains(user)) {
            LOG.error("[LobbyId: {}] User {} is not allowed to access chat messages", lobbyId, user.getUsername());
            throw new IllegalAccessException("User is not allowed to access chat messages");
        }
        List<IChatMessage> chatMessages = chatStore.getChatMessages(lobbyId);
        LOG.info("[LobbyId: {}] Chat messages retrieved", lobbyId);
        return chatMessages;
    }
}
