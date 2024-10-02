package de.uol.swp.common.game.message.response;

import de.uol.swp.common.game.message.AbstractGameResponse;

public class StatusResponse extends AbstractGameResponse {
    public StatusResponse(boolean success, String description) {
        super(success, description);
    }
}
