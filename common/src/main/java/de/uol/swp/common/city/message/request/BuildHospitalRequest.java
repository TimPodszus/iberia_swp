package de.uol.swp.common.city.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

@Getter
public class BuildHospitalRequest extends AbstractGameRequest {
    Integer cityId;

    public BuildHospitalRequest(String lobbyId, Integer cityId) {
        super(lobbyId);
        this.cityId = cityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BuildHospitalRequest that = (BuildHospitalRequest) o;
        return Objects.equals(this.getLobbyId(), that.getLobbyId()) && Objects.equals(cityId, that.cityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getLobbyId(), cityId);
    }
}
