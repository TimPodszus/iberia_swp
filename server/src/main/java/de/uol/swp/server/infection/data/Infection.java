package de.uol.swp.server.infection.data;

import de.uol.swp.common.game.PlagueName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Infection implements IInfection {
    @Setter
    private int severity;
    private final PlagueName plagueName;

    @Override
    public void decreaseSeverity(int amount) {
        if(severity - amount < 0) {
            throw new IllegalArgumentException("Severity cannot be negative");
        }
        severity -= amount;
    }
}
