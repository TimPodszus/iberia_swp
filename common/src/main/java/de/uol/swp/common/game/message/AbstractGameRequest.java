package de.uol.swp.common.game.message;

import de.uol.swp.common.message.request.AbstractRequestMessage;
import lombok.Getter;

@Getter
public abstract class AbstractGameRequest extends AbstractRequestMessage {
    private String lobbyCode;

    public AbstractGameRequest(String lobbyCode) {
        this.lobbyCode = lobbyCode;
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}
