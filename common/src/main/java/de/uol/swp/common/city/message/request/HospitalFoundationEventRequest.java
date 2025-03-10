package de.uol.swp.common.city.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

/**
 * A request to build a hospital
 */
@Getter
public class HospitalFoundationEventRequest extends AbstractGameRequest {
    /**
     * The ID of the city where the hospital should be built
     */
    private final Integer cityId;

    /**
     * Requests the foundation of a hospital
     *
     * @param lobbyId the lobbyId
     * @param cityId  the cityId
     */
    public HospitalFoundationEventRequest(String lobbyId, Integer cityId) {
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
        HospitalFoundationEventRequest that = (HospitalFoundationEventRequest) o;
        return Objects.equals(getLobbyId(), that.getLobbyId()) && Objects.equals(cityId, that.cityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId(), cityId);
    }
}
