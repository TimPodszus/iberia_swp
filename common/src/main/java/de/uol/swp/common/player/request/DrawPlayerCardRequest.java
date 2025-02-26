package de.uol.swp.common.player.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

@Getter
public class DrawPlayerCardRequest extends AbstractGameRequest {
    public DrawPlayerCardRequest(String lobbyCode) {
        super(lobbyCode);
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
