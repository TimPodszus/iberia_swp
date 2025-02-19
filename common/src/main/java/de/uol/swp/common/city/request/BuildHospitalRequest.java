package de.uol.swp.common.city.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

@Getter
public class BuildHospitalRequest extends AbstractGameRequest {
    Integer cityId;

    public BuildHospitalRequest(String lobbyId, Integer cityId) {
        super(lobbyId);
        this.cityId = cityId;
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
