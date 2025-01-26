package de.uol.swp.common.infection;

import de.uol.swp.common.game.PlagueName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class InfectionDTO implements IInfectionDTO, Serializable {
    private int severity;
    private final PlagueName plagueName;
}
