package de.uol.swp.server.infection;

import de.uol.swp.server.infection.data.IInfection;
import de.uol.swp.server.plague.data.IPlague;
import lombok.*;

@AllArgsConstructor
@Getter
public class Infection implements IInfection {
    @Setter
    private int severity;
    private final IPlague plague;
}
