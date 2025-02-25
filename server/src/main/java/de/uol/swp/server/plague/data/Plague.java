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

    public void increaseCubes(int amount) {
        if (cubesRemaining + amount > 24) {
            throw new IllegalArgumentException("Amount of cubes cannot exceed 24");
        }
        cubesRemaining += amount;
    }
}