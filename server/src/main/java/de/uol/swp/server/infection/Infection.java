package de.uol.swp.server.infection;

import de.uol.swp.server.plague.Plague;
import lombok.*;

@AllArgsConstructor
@Getter
public class Infection implements IInfection {
    @Setter
    private int severity;
    private final Plague plague;
}
