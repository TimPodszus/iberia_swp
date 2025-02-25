package de.uol.swp.common.chat;

public class SendChatRequest extends AbstractChatMessage {

    public SendChatRequest(String lobbyId, String message, String sender) {
        super(lobbyId, message, sender);
    }
}
