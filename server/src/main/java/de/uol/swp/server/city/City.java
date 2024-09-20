package de.uol.swp.server.city;

import de.uol.swp.server.plague.PlagueName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class City
{
    private final PlagueName plagueName;
    private final CityName name;
    private final int foundationDate;
    private final boolean isHarbourCity;
    @Setter
    private boolean hasHospital;
}
