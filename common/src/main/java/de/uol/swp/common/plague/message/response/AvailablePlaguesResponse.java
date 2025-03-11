package de.uol.swp.common.plague.message.response;

import de.uol.swp.common.game.message.AbstractGameResponse;
import de.uol.swp.common.infection.IInfectionDTO;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * Response containing the available plagues in a specific city within a game session.
 */
@Getter
public class AvailablePlaguesResponse extends AbstractGameResponse {

    /**
     * The list of available plagues in the specified city.
     */
    private final List<IInfectionDTO> availablePlagues;

    /**
     * The ID of the city for which the available plagues are provided.
     */
    private final int cityId;

    public AvailablePlaguesResponse(String lobbyId, boolean success, List<IInfectionDTO> availablePlagues, int cityId) {
        super(lobbyId, success);
        this.availablePlagues = availablePlagues;
        this.cityId = cityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AvailablePlaguesResponse that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return getCityId() == that.getCityId() && isSuccess() == that.isSuccess() && getLobbyId().equals(that.getLobbyId()) && Objects.equals(
                getAvailablePlagues(),
                that.getAvailablePlagues()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLobbyId(), isSuccess(), getAvailablePlagues(), getCityId());
    }
}

