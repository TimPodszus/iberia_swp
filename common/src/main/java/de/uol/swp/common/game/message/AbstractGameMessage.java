package de.uol.swp.common.game.message;

import de.uol.swp.common.message.AbstractServerMessage;
import lombok.Getter;

@Getter
public abstract class AbstractGameMessage extends AbstractServerMessage {
    private final String lobbyCode;

    protected AbstractGameMessage(String lobbyCode) {
        this.lobbyCode = lobbyCode;
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}
