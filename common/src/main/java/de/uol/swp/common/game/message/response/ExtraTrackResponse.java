package de.uol.swp.common.game.message.response;

import de.uol.swp.common.connection.IConnectionDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class ExtraTrackResponse extends AbstractGameResponse {
    private final List<IConnectionDTO> connections;
    public ExtraTrackResponse(String lobbyId, boolean success, List<IConnectionDTO> connections) {
        super(lobbyId, success);
        this.connections = connections;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ExtraTrackResponse that = (ExtraTrackResponse) o;
        return Objects.equals(connections, that.connections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), connections);
    }
}
