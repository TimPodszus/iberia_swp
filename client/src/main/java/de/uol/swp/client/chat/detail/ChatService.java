package de.uol.swp.client.chat.detail;

import com.google.inject.Inject;
import de.uol.swp.common.chat.request.SendChatRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

/**
 * Service for handling chat-related operations.
 * Responsible for sending chat messages to the server.
 */
public class ChatService {
    private static final Logger LOG = LogManager.getLogger(ChatService.class);
    private final EventBus eventBus;

    /**
     * Constructs a new ChatService with the specified EventBus.
     *
     * @param eventBus the EventBus used for posting chat requests
     */
    @Inject
    public ChatService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * Sends a chat message to the specified lobby.
     * Posts a SendChatRequest event to the EventBus.
     *
     * @param lobbyId the ID of the lobby to send the message to
     * @param message the chat message to be sent
     */
    public void sendChatMessage(String lobbyId, String message) {
        LOG.debug("[LobbyId: {}] Sending SendChatRequest with message: {}", lobbyId, message);
        eventBus.post(new SendChatRequest(lobbyId, message));
    }
}