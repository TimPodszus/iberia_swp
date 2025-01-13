package de.uol.swp.common.chat;

public class ChatRequest extends AbstractChatMessage {
    public ChatRequest(String lobbyCode, String username, String message) {
        super(lobbyCode, username, message);
    }
}
