package de.uol.swp.common.region.message.response;

import de.uol.swp.common.message.response.AbstractResponseMessage;
import de.uol.swp.common.region.IRegionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.Set;

@Getter
@AllArgsConstructor
public class AvailableRegionsResponse  extends AbstractResponseMessage {
    private Set<IRegionDTO> regions;

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
