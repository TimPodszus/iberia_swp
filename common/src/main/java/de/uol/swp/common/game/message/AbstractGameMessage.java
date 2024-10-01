package de.uol.swp.common.game.message;

import de.uol.swp.common.message.AbstractMessage;
import lombok.Getter;

@Getter
public abstract class AbstractGameMessage extends AbstractMessage {
    private String lobbyCode;

    public AbstractGameMessage(String lobbyCode) {
        this.lobbyCode = lobbyCode;
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}
