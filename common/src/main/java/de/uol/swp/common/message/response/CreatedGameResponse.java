package de.uol.swp.common.message.response;

import de.uol.swp.common.game.message.AbstractGameMessage;

public class CreatedGameResponse extends AbstractGameMessage {
    public CreatedGameResponse(String lobbyCode) {
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
