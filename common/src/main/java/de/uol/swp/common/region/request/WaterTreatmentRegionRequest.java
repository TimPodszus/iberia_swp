package de.uol.swp.common.region.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;
import java.util.Objects;

@Getter
public class WaterTreatmentRegionRequest extends AbstractGameRequest {
    private final int regionId;

    public WaterTreatmentRegionRequest(String lobbyId, int regionId) {
        super(lobbyId);
        this.regionId = regionId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WaterTreatmentRegionRequest that = (WaterTreatmentRegionRequest) o;
        return regionId == that.regionId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(regionId);
    }
}

