package de.uol.swp.server.chat.data;

import lombok.Getter;

import java.util.Objects;

/**
 * Represents a chat message sent by a player.
 */
@Getter
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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        PlayerChatMessage that = (PlayerChatMessage) object;
        return super.equals(that) && Objects.equals(sender, that.sender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sender);
    }
}
