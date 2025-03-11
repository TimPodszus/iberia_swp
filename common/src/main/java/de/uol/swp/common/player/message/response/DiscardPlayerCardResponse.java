package de.uol.swp.common.player.message.response;

import de.uol.swp.common.game.message.AbstractGameResponse;

/**
 * Response for discarding a player card.
 */
public class DiscardPlayerCardResponse extends AbstractGameResponse {

    /**
     * Constructs a new DiscardPlayerCardResponse.
     *
     * @param lobbyId     the ID of the lobby
     * @param success     whether the discard operation was successful
     * @param description a description of the response
     */
    public DiscardPlayerCardResponse(String lobbyId, boolean success, String description) {
        super(lobbyId, success, description);
    }
}