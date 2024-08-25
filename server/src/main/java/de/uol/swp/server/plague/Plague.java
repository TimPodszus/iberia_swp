package de.uol.swp.server.plague;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class Plague {
    private final String name;
    @Setter
    private int cubesRemaining;
    @Setter
    private boolean researched;
}