package de.uol.swp.common.player.message.event;

import de.uol.swp.common.game.message.AbstractGameEvent;
import de.uol.swp.common.region.IRegionDTO;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class RegionsForPreventionMarkerEvent extends AbstractGameEvent {
    List<IRegionDTO> regions;

    /**
     * Constructor of the RegionsForPreventionMarkerEvent
     * @param lobbyId The lobbyId of the lobby
     * @param regions The regions possible for the prevention marker
     */
    public RegionsForPreventionMarkerEvent(String lobbyId, List<IRegionDTO> regions) {
        super(lobbyId);
        this.regions = regions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RegionsForPreventionMarkerEvent that = (RegionsForPreventionMarkerEvent) o;
        return Objects.equals(regions, that.regions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), regions);
    }
}
