package de.uol.swp.server.city;

import de.uol.swp.server.plague.PlaqueName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class City
{
    private final PlaqueName plaqueName;
    private final String name;
    private final int foundationDate;
    private final boolean isHarbourCity;
    @Setter
    private boolean hasHospital;
}
