package de.uol.swp.common.message.gamemessage;

import de.uol.swp.common.message.AbstractMessage;
import lombok.Getter;
import java.util.Objects;

@Getter
public abstract class AbstractGameMessage extends AbstractMessage {
    private String lobbyCode;

    public AbstractGameMessage(String lobbyCode) {
        this.lobbyCode = lobbyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AbstractGameMessage that = (AbstractGameMessage) o;
        return lobbyCode.equals(that.lobbyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lobbyCode);
    }
}
