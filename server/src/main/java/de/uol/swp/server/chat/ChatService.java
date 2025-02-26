package de.uol.swp.server.chat;

import com.google.inject.Inject;
import de.uol.swp.common.chat.messages.AbstractChatMessage;
import de.uol.swp.common.chat.messages.PlayerChatMessage;
import de.uol.swp.common.chat.request.GetChatRequest;
import de.uol.swp.common.chat.request.SendChatRequest;
import de.uol.swp.common.chat.messages.ServerChatMessage;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.chat.data.IChatMessage;
import de.uol.swp.server.chat.management.IChatManagement;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.lobby.management.LobbyManagement;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.User;
import de.uol.swp.server.usermanagement.UserMapper;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChatService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(ChatService.class);
    private final ILobbyManagement lobbyManagement;
    private final IChatManagement chatManagement;

    @Inject
    public ChatService(EventBus bus, LobbyManagement lobbyManagement, IChatManagement chatManagement) {
        super(bus);
        this.lobbyManagement = lobbyManagement;
        this.chatManagement = chatManagement;
    }

    /**
     * Handles the SendChatRequest event.
     *
     * @param request the SendChatRequest containing the chat message details
     */
    @Subscribe
    public void onSendChatRequest(SendChatRequest request) {
        LOG.debug("[LobbyId: {} Received chat message", request.getLobbyId());
        Session session = request.getSession()
                                 .orElseThrow(() -> {
                                     LOG.error("[LobbyId: {}] Session not found", request.getLobbyId());
                                     return new SessionNotFoundException();
                                 });
        IUser user = UserMapper.toUser(session.getUser());
        IChatMessage chatMessage = chatManagement.addChatMessage(request.getLobbyId(),
                user.getUsername(),
                request.getMessage()
        );

        LOG.debug("[LobbyId: {}] Chat message from {} added", chatMessage.getLobbyId(), user.getUsername());

        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        AbstractChatMessage message = convertToChatMessage(chatMessage);
        sendToAllInLobby(lobby, message);
        LOG.info("[LobbyId: {}] Chat message sent to all users in lobby", chatMessage.getLobbyId());
    }

    @Subscribe
    public void onGetChatRequest(GetChatRequest request) {
        LOG.debug("[LobbyId: {}] Received request to get chat messages", request.getLobbyId());
        Session session = request.getSession()
                                 .orElseThrow(() -> {
                                     LOG.error("[LobbyId: {}] Session not found", request.getLobbyId());
                                     return new SessionNotFoundException();
                                 });
        IUser user = UserMapper.toUser(session.getUser());


        LOG.info("[LobbyId: {}] Chat messages sent to user", request.getLobbyId());
    }

    private AbstractChatMessage convertToChatMessage(IChatMessage chatMessage) {
        if (chatMessage instanceof PlayerChatMessage playerChatMessage) {
            return new PlayerChatMessage(chatMessage.getLobbyId(),
                    playerChatMessage.getSender(),
                    playerChatMessage.getMessage()
            );
        } else {
            return new ServerChatMessage(chatMessage.getLobbyId(), chatMessage.getMessage());
        }
    }

}
