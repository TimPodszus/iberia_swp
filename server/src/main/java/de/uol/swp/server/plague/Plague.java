package de.uol.swp.server.plague;

import lombok.*;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class Plague {
    static String name;
    static int cubesRemaining;
    @Setter
    static boolean researched;
}