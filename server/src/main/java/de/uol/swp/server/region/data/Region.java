package de.uol.swp.server.region.data;

import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.region.management.RegionManagementException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Region implements IRegion {
    private final int id;
    private final List<ICity> surroundingCities;
    private int waterTreatments;
    private boolean preventionMarker;

    public void increaseWaterTreatments(int amount) {
        waterTreatments += amount;
    }

    public void decreaseWaterTreatments(int amount) throws RegionManagementException {
        if (waterTreatments >= amount) {
            waterTreatments -= amount;
        } else {
            throw new RegionManagementException("Nicht genug Wasseraufbereitungsmarker in dieser Region verfügbar");
        }
    }
}
