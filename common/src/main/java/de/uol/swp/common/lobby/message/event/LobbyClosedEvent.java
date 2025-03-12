package de.uol.swp.common.lobby.message.event;

import de.uol.swp.common.message.AbstractServerMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class LobbyClosedEvent extends AbstractServerMessage {
    private String lobbyId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        LobbyClosedEvent that = (LobbyClosedEvent) o;
        return Objects.equals(lobbyId, that.lobbyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lobbyId);
    }
}
