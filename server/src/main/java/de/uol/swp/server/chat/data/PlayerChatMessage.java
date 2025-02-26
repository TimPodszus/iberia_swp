package de.uol.swp.server.chat.data;

/**
 * Represents a chat message sent by a player.
 */
public class PlayerChatMessage extends ChatMessage {
    String sender;

    /**
     * Constructs a new PlayerChatMessage.
     *
     * @param sender the sender of the chat message
     * @param message the content of the chat message
     */
    public PlayerChatMessage(String sender, String message) {
        super(message);
        this.sender = sender;
    }
}
