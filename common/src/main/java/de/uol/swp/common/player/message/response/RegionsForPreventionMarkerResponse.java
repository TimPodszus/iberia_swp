package de.uol.swp.common.player.message.response;

import de.uol.swp.common.game.message.AbstractGameResponse;
import de.uol.swp.common.region.IRegionDTO;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class RegionsForPreventionMarkerResponse extends AbstractGameResponse {
    List<IRegionDTO> regions;

    public RegionsForPreventionMarkerResponse(String lobbyId, boolean success, List<IRegionDTO> regions) {
        super(lobbyId, success);
        this.regions = regions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RegionsForPreventionMarkerResponse that = (RegionsForPreventionMarkerResponse) o;
        return Objects.equals(regions, that.regions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), regions);
    }
}
