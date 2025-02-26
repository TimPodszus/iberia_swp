package de.uol.swp.common.chat.messages;

/**
 * Represents a chat message sent by the server.
 * Extends the AbstractChatMessage class.
 */
public class ServerChatMessage extends AbstractChatMessage {

    /**
     * Constructs a new ServerChatMessage with the specified lobby ID and message content.
     *
     * @param lobbyId the ID of the lobby where the message was sent
     * @param message the content of the message
     */
    public ServerChatMessage(String lobbyId, String message) {
        super(lobbyId, message);
    }
}