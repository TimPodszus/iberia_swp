package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import de.uol.swp.common.game.message.event.ShareKnowledgeEvent;
import lombok.Getter;

import java.util.Objects;
@Getter
public class ShareKnowledgeRequest extends AbstractGameRequest {
    ShareKnowledgeEvent shareKnowledgeEvent;
    boolean success;

    public ShareKnowledgeRequest(String lobbyId, boolean success, ShareKnowledgeEvent shareKnowledgeEvent) {
        super(lobbyId);
        this.success = success;
        this.shareKnowledgeEvent = shareKnowledgeEvent;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShareKnowledgeRequest that = (ShareKnowledgeRequest) o;
        return success == that.success &&
                Objects.equals(getLobbyId(), that.getLobbyId()) &&
                Objects.equals(shareKnowledgeEvent, that.shareKnowledgeEvent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId(), success, shareKnowledgeEvent);
    }
    public boolean isAccepted() {
        return success;
    }
}
