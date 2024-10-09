package de.uol.swp.common.infection;

import de.uol.swp.common.plague.IPlagueDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InfectionDTO implements IInfectionDTO {
    private int severity;
    private final IPlagueDTO plague;
}
