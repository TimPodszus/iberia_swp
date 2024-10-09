package de.uol.swp.common.plague;

import de.uol.swp.common.game.PlagueName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlagueDTO implements IPlagueDTO {
    private final PlagueName name;
    private int cubesRemaining;
    private boolean researched;
}
