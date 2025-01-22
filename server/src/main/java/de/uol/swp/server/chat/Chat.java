package de.uol.swp.server.chat;

import de.uol.swp.common.chat.AbstractChatMessage;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private final List<AbstractChatMessage> messages = new ArrayList<>();

    public void addMessage(AbstractChatMessage message) {
        messages.add(message);
    }

    public List<AbstractChatMessage> getMessages() {
        return new ArrayList<>(messages);
    }
}
