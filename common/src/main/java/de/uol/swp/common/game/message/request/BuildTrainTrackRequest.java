package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

@Getter
public class BuildTrainTrackRequest extends AbstractGameRequest {
    private final int connectionId;

    public BuildTrainTrackRequest(String lobbyCode, int connectionId) {
        super(lobbyCode);
        this.connectionId = connectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BuildTrainTrackRequest that = (BuildTrainTrackRequest) o;
        return connectionId == that.connectionId && Objects.equals(getLobbyId(), that.getLobbyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionId);
    }
}
