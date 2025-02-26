package de.uol.swp.server.chat.data;

/**
 * Represents a chat message sent by a player.
 */
public class PlayerChatMessage extends ChatMessage {
    String sender;

    /**
     * Constructs a new PlayerChatMessage.
     *
     * @param lobbyId the ID of the lobby the chat message belongs to
     * @param sender the sender of the chat message
     * @param message the content of the chat message
     */
    public PlayerChatMessage(String lobbyId, String sender, String message) {
        super(lobbyId, message);
        this.sender = sender;
    }
}
