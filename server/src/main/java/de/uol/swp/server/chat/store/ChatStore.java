package de.uol.swp.server.chat.store;

import com.google.inject.Singleton;
import de.uol.swp.server.chat.data.IChatMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class ChatStore implements IChatStore {
    /*
     * Stores the chat messages.
     * The key is the lobbyId and the value is a list of chat messages.
     */
    private static final Map<String, List<IChatMessage>> chatMessages = new HashMap<>();

    @Override
    public List<IChatMessage> getChatMessages(String lobbyId) {
        return chatMessages.get(lobbyId);
    }

    @Override
    public void addChatMessage(String lobbyId, IChatMessage chatMessage) {
        if (!chatMessages.containsKey(lobbyId)) {
            chatMessages.put(lobbyId, List.of(chatMessage));
        } else {
            chatMessages.get(lobbyId)
                        .add(chatMessage);
        }
    }
}
