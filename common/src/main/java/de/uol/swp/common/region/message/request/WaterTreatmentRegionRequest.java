package de.uol.swp.common.region.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

/**
 * Request to perform water treatment in a specific region.
 */
@Getter
public class WaterTreatmentRegionRequest extends AbstractGameRequest {
    private final int regionId;

    /**
     * Constructs a new WaterTreatmentRegionRequest with the specified lobby ID and region ID.
     *
     * @param lobbyId  the ID of the lobby
     * @param regionId the ID of the region
     */
    public WaterTreatmentRegionRequest(String lobbyId, int regionId) {
        super(lobbyId);
        this.regionId = regionId;
    }

    /**
     * Checks if this request is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaterTreatmentRegionRequest that = (WaterTreatmentRegionRequest) o;
        return regionId == that.regionId && getLobbyId().equals(that.getLobbyId());
    }

    /**
     * Returns the hash code of this request.
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId(), regionId);
    }
}