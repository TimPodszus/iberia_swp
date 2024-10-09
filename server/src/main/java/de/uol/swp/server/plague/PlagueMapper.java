package de.uol.swp.server.plague;

import de.uol.swp.common.plague.IPlagueDTO;
import de.uol.swp.common.plague.PlagueDTO;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PlagueMapper {
    public static IPlagueDTO toDTO(IPlague plague) {
        return new PlagueDTO(
                plague.getName(),
                plague.getCubesRemaining(),
                plague.isResearched()
        );
    }
}
