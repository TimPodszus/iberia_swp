package de.uol.swp.common.city.message.response;

import de.uol.swp.common.message.response.AbstractResponseMessage;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * A response to let the user choose a city to build the hospital in
 */
@Getter
public class HospitalFoundationEventResponse extends AbstractResponseMessage {
    /**
     * The ID of the lobby
     */
    private final String lobbyId;

    /**
     * The IDs of the possible cities
     */
    private final List<Integer> cityIds;

    /**
     * Constructs a new HospitalFoundationEventResponse.
     *
     * @param lobbyId the ID of the lobby
     * @param cityIds the IDs of the cities
     */
    public HospitalFoundationEventResponse(String lobbyId, List<Integer> cityIds) {
        this.lobbyId = lobbyId;
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
        return Objects.equals(lobbyId, that.lobbyId) && Objects.equals(cityIds, that.cityIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lobbyId, cityIds);
    }
}