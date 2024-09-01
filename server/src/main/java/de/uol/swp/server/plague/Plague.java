package de.uol.swp.server.plague;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
public class Plague
{
    private final PlagueName name;
    @Setter
    private int cubesRemaining;
    @Setter
    private boolean researched;
}