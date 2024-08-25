package de.uol.swp.server.city;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class City {
    private final String name;
    private final int foundationDate;
    private final boolean isHarbourCity;
    @Setter
    private boolean hasHospital;

}
