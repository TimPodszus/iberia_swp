package de.uol.swp.server.plague.data;

import de.uol.swp.common.game.PlagueName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
public class Plague implements IPlague {
    private final PlagueName name;
    @Setter
    private int cubesRemaining;
    @Setter
    private boolean researched;

    private final String color;
}