package de.uol.swp.common.chat.request;

import de.uol.swp.common.message.request.AbstractRequestMessage;
import lombok.Getter;

import java.util.Objects;

/**
 * Request to get chat messages for a specific lobby.
 */
@Getter
public class GetChatRequest extends AbstractRequestMessage {

    /**
     * The ID of the lobby for which to get chat messages.
     */
    private final String lobbyId;

    /**
     * Constructs a new GetChatRequest.
     *
     * @param lobbyId the ID of the lobby
     */
    public GetChatRequest(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        GetChatRequest that = (GetChatRequest) object;
        return super.equals(that) && Objects.equals(lobbyId, that.lobbyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lobbyId);
    }
}
