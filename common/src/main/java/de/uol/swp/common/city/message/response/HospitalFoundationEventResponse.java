package de.uol.swp.common.city.message.response;

import de.uol.swp.common.game.message.AbstractGameResponse;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * A response to let the user choose a city to build the hospital in
 */
@Getter
public class HospitalFoundationEventResponse extends AbstractGameResponse {

    /**
     * The IDs of the possible cities
     */
    private final List<Integer> cityIds;

    /**
     * Constructs a new HospitalFoundationEventResponse.
     *
     * @param cityIds the IDs of the cities
     */
    public HospitalFoundationEventResponse(String lobbyId, List<Integer> cityIds) {
        super(lobbyId, true);
        this.cityIds = cityIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HospitalFoundationEventResponse that = (HospitalFoundationEventResponse) o;
        return Objects.equals(cityIds, that.cityIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityIds);
    }
}