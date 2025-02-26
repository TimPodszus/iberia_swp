package de.uol.swp.server.chat;

import com.google.inject.Inject;
import de.uol.swp.common.chat.messages.AbstractChatMessage;
import de.uol.swp.common.chat.messages.ServerChatMessage;
import de.uol.swp.common.chat.request.GetChatRequest;
import de.uol.swp.common.chat.request.SendChatRequest;
import de.uol.swp.common.chat.response.GetChatResponse;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.chat.data.IChatMessage;
import de.uol.swp.server.chat.data.PlayerChatMessage;
import de.uol.swp.server.chat.event.ServerMessageEvent;
import de.uol.swp.server.chat.management.IChatManagement;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.UserMapper;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class ChatService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(ChatService.class);
    protected ILobbyManagement lobbyManagement;
    protected IChatManagement chatManagement;

    @Inject
    public ChatService(EventBus bus, ILobbyManagement lobbyManagement, IChatManagement chatManagement) {
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

    /**
     * Handles the GetChatRequest event.
     *
     * @param request the GetChatRequest containing the details of the request
     */
    @Subscribe
    public void onGetChatRequest(GetChatRequest request) {
        LOG.debug("[LobbyId: {}] Received request to get chat messages", request.getLobbyId());
        Session session = request.getSession()
                                 .orElseThrow(() -> {
                                     LOG.error("[LobbyId: {}] Session not found", request.getLobbyId());
                                     return new SessionNotFoundException();
                                 });
        IUser user = UserMapper.toUser(session.getUser());

        try {
            List<IChatMessage> chatMessages = chatManagement.getChatMessages(request.getLobbyId(), user);
            List<AbstractChatMessage> messages = chatMessages.stream()
                                                             .map(this::convertToChatMessage)
                                                             .toList();
            GetChatResponse response = new GetChatResponse(request.getLobbyId(), messages);
            response.setSession(session);
            request.getMessageContext()
                   .ifPresent(response::setMessageContext);
            LOG.info("[LobbyId: {}] Chat messages sent to user", request.getLobbyId());
            post(response);
        } catch (IllegalAccessException e) {
            LOG.error("[LobbyId: {}] User is not allowed to access chat messages", request.getLobbyId());
            StatusResponse response = new StatusResponse(request.getLobbyId(),
                    false,
                    "Nutzer hat keinen Zugriff auf die angefragten Chat-Nachrichten"
            );
            response.setSession(session);
            request.getMessageContext()
                   .ifPresent(response::setMessageContext);
            post(response);
        }
    }

    /**
     * Handles the ServerMessageEvent event.
     *
     * @param event the ServerMessageEvent containing the server message details
     */
    @Subscribe
    public void onServerMessageEvent(ServerMessageEvent event) {
        LOG.debug("[LobbyId: {}] Received server message", event.getLobbyId());
        IChatMessage chatMessage = chatManagement.addChatMessage(event.getLobbyId(), event.getMessage());

        AbstractChatMessage message = convertToChatMessage(chatMessage);
        ILobby lobby = lobbyManagement.getLobby(event.getLobbyId());
        sendToAllInLobby(lobby, message);
    }

    /**
     * Converts an IChatMessage to an AbstractChatMessage.
     *
     * @param chatMessage the IChatMessage to convert
     * @return the converted AbstractChatMessage
     */
    private AbstractChatMessage convertToChatMessage(IChatMessage chatMessage) {
        if (chatMessage instanceof PlayerChatMessage playerChatMessage) {
            return new de.uol.swp.common.chat.messages.PlayerChatMessage(
                    chatMessage.getLobbyId(),
                    playerChatMessage.getSender(),
                    playerChatMessage.getMessage()
            );
        } else {
            return new ServerChatMessage(chatMessage.getLobbyId(), chatMessage.getMessage());
        }
    }

}
