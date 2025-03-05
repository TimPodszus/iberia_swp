package de.uol.swp.server.chat.data;

/**
 * Represents a server chat message.
 */
public class ServerChatMessage extends ChatMessage {

    /**
     * Constructs a new ServerChatMessage with the specified message.
     *
     * @param lobbyId the ID of the lobby the chat message belongs to
     * @param message the message content
     */
    public ServerChatMessage(String lobbyId, String message) {
        super(lobbyId, message);
    }
}
