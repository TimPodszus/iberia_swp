package de.uol.swp.server.city;

import de.uol.swp.common.enums.PlagueName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class City {
    private final int id;
    private final PlagueName plagueName;
    private final CityName name;
    private final int foundationDate;
    private final boolean isHarbourCity;
    @Setter
    private boolean hasHospital;

    @Override
    public boolean equals(Object object) {
        boolean equals = false;
        if (object instanceof City city) {
            equals = this.plagueName.equals(city.plagueName) && this.name.equals(city.name) && this.foundationDate == city.foundationDate && this.isHarbourCity == city.isHarbourCity && this.hasHospital == city.hasHospital;
        }
        return equals;
    }

    @Override
    public int hashCode() {
        return plagueName.hashCode() + name.hashCode() + foundationDate + (isHarbourCity ? 1 : 0) + (hasHospital ? 1 : 0);
    }

}
