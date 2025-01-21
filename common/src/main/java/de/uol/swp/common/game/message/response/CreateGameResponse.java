package de.uol.swp.common.game.message.response;

import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.Objects;

/**
 * Response message for creating a game.
 * <p>
 * This class represents a response message that is sent when a game is created.
 * It extends the AbstractGameResponse class.
 */
@Getter
public class CreateGameResponse extends AbstractGameResponse {

    /**
     * Constructs a new CreateGameResponse.
     *
     * @param lobbyId     the ID of the lobby
     * @param success     indicates whether the game creation was successful
     * @param description a description of the game creation result
     */
    public CreateGameResponse(String lobbyId, boolean success, String description) {
        super(lobbyId, success, description);
    }
}