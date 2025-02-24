package de.uol.swp.server.cards.events;

import de.uol.swp.common.message.AbstractMessage;
import de.uol.swp.server.message.ServerInternalMessage;
import lombok.Getter;

import java.util.Objects;

@Getter
public class StateMobilizationEvent extends AbstractMessage implements ServerInternalMessage {
    private final String lobbyId;

    public StateMobilizationEvent(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        StateMobilizationEvent that = (StateMobilizationEvent) object;
        return Objects.equals(lobbyId, that.lobbyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lobbyId);
    }
}
