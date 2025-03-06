package de.uol.swp.common.player.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

/**
 * Request to draw a player card.
 */
@Getter
public class DrawPlayerCardRequest extends AbstractGameRequest {
    
    /**
     * Constructs a new DrawPlayerCardRequest.
     *
     * @param lobbyId the ID of the lobby
     */
    public DrawPlayerCardRequest(String lobbyId) {
        super(lobbyId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DrawPlayerCardRequest that = (DrawPlayerCardRequest) o;
        return Objects.equals(getLobbyId(), that.getLobbyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId());
    }
}