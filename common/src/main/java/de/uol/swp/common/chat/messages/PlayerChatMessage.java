package de.uol.swp.common.chat.messages;

import lombok.Getter;

import java.util.Objects;

/**
 * Represents a chat message sent by a player.
 * Extends the AbstractChatMessage class.
 */
@Getter
public class PlayerChatMessage extends AbstractChatMessage {

    /**
     * The sender of the chat message.
     */
    private final String sender;

    /**
     * Constructs a new PlayerChatMessage with the specified lobby ID, sender, and message content.
     *
     * @param lobbyId the ID of the lobby where the message was sent
     * @param sender  the sender of the message
     * @param message the content of the message
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
