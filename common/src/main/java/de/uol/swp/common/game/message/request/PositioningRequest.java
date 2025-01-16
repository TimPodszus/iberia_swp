package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

@Getter
public class PositioningRequest extends AbstractGameRequest {
    int cityId;

    public PositioningRequest(String lobbyCode, int cityId) {
        super(lobbyCode);
        this.cityId = cityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositioningRequest that = (PositioningRequest) o;
        return getCityId() == that.getCityId() &&
                Objects.equals(getLobbyCode(), that.getLobbyCode());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCityId());
    }
}
