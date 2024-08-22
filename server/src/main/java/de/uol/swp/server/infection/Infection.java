package de.uol.swp.server.infection;

import de.uol.swp.server.plague.Plague;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter

public class Infection
{
    @Setter
    private static int severity;
    private final Plague plague;
}
