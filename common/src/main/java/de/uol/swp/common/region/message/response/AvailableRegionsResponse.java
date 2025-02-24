package de.uol.swp.common.region.message.response;

import de.uol.swp.common.game.message.AbstractGameResponse;
import de.uol.swp.common.region.IRegionDTO;
import lombok.Getter;

import java.util.Objects;
import java.util.Set;

/**
 * A response containing the available regions.
 */
@Getter
public class AvailableRegionsResponse extends AbstractGameResponse {
    private final Set<IRegionDTO> regions;

    /**
     * Constructs a new AvailableRegionsResponse.
     *
     * @param lobbyId the ID of the lobby
     * @param regions the set of available regions
     */
    public AvailableRegionsResponse(String lobbyId, Set<IRegionDTO> regions) {
        super(lobbyId, true, "");
        this.regions = regions;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AvailableRegionsResponse that = (AvailableRegionsResponse) o;
        return Objects.equals(getRegions(), that.getRegions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getRegions());
    }
}
