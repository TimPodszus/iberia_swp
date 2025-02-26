package de.uol.swp.server.chat.data;

import lombok.Getter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ChatMessage that = (ChatMessage) object;
        return Objects.equals(lobbyId, that.lobbyId) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lobbyId, message);
    }
}
