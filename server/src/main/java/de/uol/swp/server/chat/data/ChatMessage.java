package de.uol.swp.server.chat.data;

import lombok.Getter;

/**
 * Abstract class representing a chat message.
 * Implements the IChatMessage interface and provides a base implementation for chat messages.
 */
@Getter
public abstract class ChatMessage implements IChatMessage {

    private final String lobbyId;

    private final String message;

    /**
     * Constructs a new ChatMessage with the specified content.
     *
     * @param lobbyId the ID of the lobby the chat message belongs to
     * @param message the content of the chat message
     */
    protected ChatMessage(String lobbyId, String message) {
        this.lobbyId = lobbyId;
        this.message = message;
    }
}
