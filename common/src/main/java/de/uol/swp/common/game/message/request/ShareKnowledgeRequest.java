package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

/**
 * Request to share knowledge in the game.
 */
@Getter
public class ShareKnowledgeRequest extends AbstractGameRequest {
    String username;

    /**
     * Constructs a new ShareKnowledgeRequest.
     *
     * @param lobbyId the ID of the lobby
     */
    public ShareKnowledgeRequest(String lobbyId, String username) {
        super(lobbyId);
        this.username = username;
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
        return Objects.equals(getLobbyId(), that.getLobbyId());


    }

    /**
     * Computes the hash code for this request.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId());
    }
}


