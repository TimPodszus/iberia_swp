package de.uol.swp.common.plague.request;

import de.uol.swp.common.game.message.AbstractGameRequest;

import java.util.Objects;

public class AvailableCitiesToTreatRequest extends AbstractGameRequest {
    private final int cityId;

    public AvailableCitiesToTreatRequest(String lobbyId, int cityId) {
        super(lobbyId);
        this.cityId = cityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvailableCitiesToTreatRequest that)) return false;
        return cityId == that.cityId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityId);
    }
}
