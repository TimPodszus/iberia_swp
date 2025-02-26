package de.uol.swp.server.chat.data;

import lombok.Getter;

/**
 * Abstract class representing a chat message.
 * Implements the IChatMessage interface and provides a base implementation for chat messages.
 */
@Getter
public abstract class ChatMessage implements IChatMessage {
    /**
     * The content of the chat message.
     */
    private final String message;

    /**
     * Constructs a new ChatMessage with the specified content.
     *
     * @param message the content of the chat message
     */
    protected ChatMessage(String message) {
        this.message = message;
    }
}
