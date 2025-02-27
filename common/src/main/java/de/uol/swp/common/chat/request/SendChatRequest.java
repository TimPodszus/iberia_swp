package de.uol.swp.common.chat.request;

import de.uol.swp.common.message.request.AbstractRequestMessage;
import lombok.Getter;

import java.util.Objects;

/**
 * A request message to send a chat message to a specific lobby.
 */
@Getter
public class SendChatRequest extends AbstractRequestMessage {

    /**
     * The ID of the lobby to which the message is sent.
     */
    private final String lobbyId;

    /**
     * The chat message to be sent.
     */
    private final String message;

    /**
     * Constructs a new SendChatRequest.
     *
     * @param lobbyId the ID of the lobby
     * @param message the chat message
     */
    public SendChatRequest(String lobbyId, String message) {
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
        SendChatRequest that = (SendChatRequest) object;
        return super.equals(that) && Objects.equals(lobbyId, that.lobbyId) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lobbyId, message);
    }
}
