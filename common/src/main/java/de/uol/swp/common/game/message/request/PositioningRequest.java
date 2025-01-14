package de.uol.swp.common.game.message.request;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

@Getter
public class PositioningRequest extends AbstractGameRequest {
    CityDTO city;

    public PositioningRequest(String lobbyCode, CityDTO city) {
        super(lobbyCode);
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
