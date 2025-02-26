package de.uol.swp.common.player.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

@Getter
public class DrawPlayerCardRequest extends AbstractGameRequest {
    public DrawPlayerCardRequest(String lobbyCode) {
        super(lobbyCode);
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
