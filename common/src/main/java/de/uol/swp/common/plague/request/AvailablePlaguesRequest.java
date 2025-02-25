package de.uol.swp.common.plague.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

@Getter
public class AvailablePlaguesRequest extends AbstractGameRequest {

    private final int cityId;

    public AvailablePlaguesRequest(String lobbyId, int cityId) {
        super(lobbyId);
        this.cityId = cityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AvailablePlaguesRequest that)) {
            return false;
        }
        return getCityId() == that.getCityId() && getLobbyId().equals(that.getLobbyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId(), getCityId());
    }
}
