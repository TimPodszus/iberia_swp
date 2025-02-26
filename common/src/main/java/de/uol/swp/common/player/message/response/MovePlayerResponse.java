package de.uol.swp.common.player.message.response;

import de.uol.swp.common.game.message.AbstractGameResponse;

/**
 * Response message for moving a player in the game.
 */
public class MovePlayerResponse extends AbstractGameResponse {

    /**
     * Constructs a new MovePlayerResponse.
     *
     * @param lobbyID     the ID of the lobby
     * @param success     whether the move was successful
     * @param description a description of the move
     */
    public MovePlayerResponse(String lobbyID, boolean success, String description) {
        super(lobbyID, success, description);
    }
}
