package de.uol.swp.common.game.message.response;

import de.uol.swp.common.game.message.AbstractGameResponse;
import de.uol.swp.common.game.message.event.ShareKnowledgeEvent;

public class ShareKnowledgeResponse extends AbstractGameResponse {
    ShareKnowledgeEvent shareKnowledgeEvent;

    public ShareKnowledgeResponse(String lobbyId,boolean success, ShareKnowledgeEvent shareKnowledgeEvent) {
        super(lobbyId, success);
        this.shareKnowledgeEvent = shareKnowledgeEvent;

    }

}
