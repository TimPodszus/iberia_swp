package de.uol.swp.common.player.request;

import de.uol.swp.common.game.message.AbstractGameResponse;

public class DiscardPlayerCardResponse extends AbstractGameResponse {

    public DiscardPlayerCardResponse(String lobbyId, boolean success, String description) {
        super(lobbyId, success, description);
    }
}
