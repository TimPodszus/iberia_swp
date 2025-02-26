package de.uol.swp.common.chat.response;

import de.uol.swp.common.chat.messages.AbstractChatMessage;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * Represents a response message containing chat messages for a specific lobby.
 * Extends the AbstractResponseMessage class.
 */
@Getter
public class GetChatResponse extends AbstractResponseMessage {
    /**
     * The ID of the lobby for which the chat messages are retrieved.
     */
    private final String lobbyId;

    /**
     * The list of chat messages retrieved for the specified lobby.
     */
    private final List<AbstractChatMessage> chatMessages;

    /**
     * Constructs a new GetChatResponse with the specified lobby ID and list of chat messages.
     *
     * @param lobbyId the ID of the lobby for which the chat messages are retrieved
     * @param chatMessages the list of chat messages retrieved for the specified lobby
     */
    public GetChatResponse(String lobbyId, List<AbstractChatMessage> chatMessages) {
        this.lobbyId = lobbyId;
        this.chatMessages = chatMessages;
    }

    /**
     * Checks if this GetChatResponse is equal to another object.
     *
     * @param object the object to compare with
     * @return true if this GetChatResponse is equal to the specified object, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        GetChatResponse that = (GetChatResponse) object;
        return super.equals(that) && Objects.equals(lobbyId, that.lobbyId) && Objects.equals(chatMessages,
                that.chatMessages);
    }

    /**
     * Returns the hash code value for this GetChatResponse.
     *
     * @return the hash code value for this GetChatResponse
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lobbyId, chatMessages);
    }
}
