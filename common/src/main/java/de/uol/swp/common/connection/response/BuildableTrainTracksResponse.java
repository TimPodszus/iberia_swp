package de.uol.swp.common.connection.response;

import de.uol.swp.common.connection.IConnectionDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class BuildableTrainTracksResponse extends AbstractGameResponse {
    private final List<IConnectionDTO> connections;

    public BuildableTrainTracksResponse(String lobbyId, boolean success, List<IConnectionDTO> connections) {
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
        BuildableTrainTracksResponse that = (BuildableTrainTracksResponse) o;
        return Objects.equals(connections, that.connections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), connections);
    }
}
