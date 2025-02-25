package de.uol.swp.common.plague.response;

import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;


import java.util.List;
import java.util.Objects;


@Getter
public class TreatPlagueResponse extends AbstractGameResponse {
    List<ICityDTO> availableCities;
    boolean countryDoctor;

    public TreatPlagueResponse(String lobbyId, boolean success, List<ICityDTO> availableCities, boolean countryDoctor) {
        super(lobbyId, success);
        this.availableCities = availableCities;
        this.countryDoctor = countryDoctor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TreatPlagueResponse that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return countryDoctor == that.countryDoctor && isSuccess() == that.isSuccess() && getLobbyId().equals(that.getLobbyId()) && Objects.equals(
                availableCities,
                that.availableCities
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLobbyId(), isSuccess(), availableCities, countryDoctor);
    }

}

