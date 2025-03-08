package de.uol.swp.common.plague.dto;

import de.uol.swp.common.game.PlagueName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class PlagueDTO implements IPlagueDTO, Serializable {
    private final PlagueName name;
    private int cubesRemaining;
    private boolean researched;
}
