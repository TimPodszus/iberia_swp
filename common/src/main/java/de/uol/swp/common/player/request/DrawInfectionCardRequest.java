package de.uol.swp.common.player.request;

import de.uol.swp.common.game.message.AbstractGameRequest;

public class DrawInfectionCardRequest extends AbstractGameRequest {

    public DrawInfectionCardRequest(String lobbyCode) {
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
