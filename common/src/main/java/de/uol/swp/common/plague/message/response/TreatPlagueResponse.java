package de.uol.swp.common.plague.message.response;

import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * Response indicating the result of a plague treatment attempt within a game session.
 */
@Getter
public class TreatPlagueResponse extends AbstractGameResponse {

    /**
     * The list of cities where further actions may be available after the treatment.
     */
    private final List<ICityDTO> availableCities;

    /**
     * Indicates whether the "country doctor" role is available or active.
     */
    private final boolean countryDoctor;

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
        return countryDoctor == that.countryDoctor && isSuccess() == that.isSuccess() && getLobbyId().equals(that.getLobbyId()) && Objects.equals(availableCities,
                that.availableCities
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLobbyId(), isSuccess(), availableCities, countryDoctor);
    }

}

