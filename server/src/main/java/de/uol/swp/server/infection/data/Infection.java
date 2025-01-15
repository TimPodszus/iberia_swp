package de.uol.swp.server.infection.data;

import de.uol.swp.server.plague.data.Plague;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Infection implements IInfection {
    @Setter
    private int severity;
    private final Plague plague;
}
