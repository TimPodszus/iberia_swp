package de.uol.swp.common.plague.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

/**
 * Request to retrieve the available plagues in a specific city within a game session.
 */
@Getter
public class AvailablePlaguesRequest extends AbstractGameRequest {

    /**
     * The ID of the city for which the available plagues are requested.
     */
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
