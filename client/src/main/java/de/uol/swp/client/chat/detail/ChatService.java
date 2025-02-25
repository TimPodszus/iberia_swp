package de.uol.swp.client.chat.detail;

import com.google.inject.Inject;
import de.uol.swp.common.chat.SendChatRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChatService {
    private static final Logger LOG = LogManager.getLogger(ChatService.class);

    private final EventBus eventBus;

    @Inject
    public ChatService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Subscribe
    public void sendChatMessage(String lobbyId, String message, String sender) {
        eventBus.post(new SendChatRequest(lobbyId, message, sender));
    }
}
