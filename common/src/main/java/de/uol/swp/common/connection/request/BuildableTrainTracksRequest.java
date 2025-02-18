package de.uol.swp.common.connection.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

@Getter
public class BuildableTrainTracksRequest extends AbstractGameRequest {
    private final int cityId;

    public BuildableTrainTracksRequest(String lobbyId, int cityId) {
        super(lobbyId);
        this.cityId = cityId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BuildableTrainTracksRequest that = (BuildableTrainTracksRequest) o;
        return Objects.equals(cityId, that.cityId) && Objects.equals(getLobbyId(), that.getLobbyId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cityId);
    }
}
