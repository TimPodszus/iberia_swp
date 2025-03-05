package de.uol.swp.common.player.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

@Getter
public class PlacePreventionMarkerRequest extends AbstractGameRequest{
    private final int regionId;

    public PlacePreventionMarkerRequest(String lobbyId, int regionId) {
        super(lobbyId);
        this.regionId = regionId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        PlacePreventionMarkerRequest that = (PlacePreventionMarkerRequest) object;
        return Objects.equals(super.getLobbyId(), that.getLobbyId()) && Objects.equals(regionId, that.regionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getLobbyId(), regionId);
    }
}
