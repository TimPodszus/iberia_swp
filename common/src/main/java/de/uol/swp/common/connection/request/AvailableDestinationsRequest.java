package de.uol.swp.common.connection.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;


@Getter
public class AvailableDestinationsRequest extends AbstractGameRequest {
    private final String cityId;

    public AvailableDestinationsRequest(String lobbyCode, String cityId) {
        super(lobbyCode);
        this.cityId = cityId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        AvailableDestinationsRequest request = (AvailableDestinationsRequest) object;
        return Objects.equals(cityId, request.cityId) && Objects.equals(getLobbyId(), request.getLobbyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityId, getLobbyId());
    }
}
