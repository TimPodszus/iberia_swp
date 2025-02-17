package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import de.uol.swp.common.game.message.event.ShareKnowledgeEvent;
import lombok.Getter;

import java.util.Objects;

/**
 * Request to share knowledge in the game.
 */
@Getter
public class ShareKnowledgeRequest extends AbstractGameRequest {
    ShareKnowledgeEvent shareKnowledgeEvent;
    boolean success;

    /**
     * Constructs a new ShareKnowledgeRequest.
     *
     * @param lobbyId             the ID of the lobby
     * @param success             whether the knowledge sharing was successful
     * @param shareKnowledgeEvent the event associated with the knowledge sharing
     */
    public ShareKnowledgeRequest(String lobbyId, boolean success, ShareKnowledgeEvent shareKnowledgeEvent) {
        super(lobbyId);
        this.success = success;
        this.shareKnowledgeEvent = shareKnowledgeEvent;
    }

    /**
     * Checks if this request is equal to another object.
     *
     * @param o the object to compare to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShareKnowledgeRequest that = (ShareKnowledgeRequest) o;
        return success == that.success && Objects.equals(getLobbyId(), that.getLobbyId()) && Objects.equals(
                shareKnowledgeEvent,
                that.shareKnowledgeEvent
        );
    }

    /**
     * Computes the hash code for this request.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId(), success, shareKnowledgeEvent);
    }

    /**
     * Checks if the knowledge sharing was accepted.
     *
     * @return true if the knowledge sharing was successful, false otherwise
     */
    public boolean isAccepted() {
        return success;
    }
}