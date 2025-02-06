package de.uol.swp.common.city.request;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

@Getter
public class BuildHospitalRequest extends AbstractGameRequest {
    CityName cityName;

    public BuildHospitalRequest(String lobbyId, CityName cityName) {
        super(lobbyId);
        this.cityName = cityName;
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
