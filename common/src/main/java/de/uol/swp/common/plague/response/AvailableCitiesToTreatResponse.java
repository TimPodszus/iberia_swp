package de.uol.swp.common.plague.response;

import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class AvailableCitiesToTreatResponse extends AbstractGameResponse {

    private final List<ICityDTO> availableCities;

    public AvailableCitiesToTreatResponse(String lobbyId, boolean success, List<ICityDTO> availableCities) {
        super(lobbyId, success);
        this.availableCities = availableCities;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvailableCitiesToTreatResponse that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getAvailableCities(), that.getAvailableCities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAvailableCities());
    }
}

