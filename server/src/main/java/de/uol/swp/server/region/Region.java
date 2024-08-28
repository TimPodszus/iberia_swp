package de.uol.swp.server.region;

import de.uol.swp.server.city.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Region {
    private final int id;
    private final List<City> surroundingCities;
    private int waterTreatments;
    private boolean preventionMarker;

    public void increaseWaterTreatments(int count) {
        waterTreatments += count;
    }

    public void decreaseWaterTreatments(int count) {
        waterTreatments -= count;
        if (waterTreatments < 0) {
            waterTreatments = 0;
        }
    }
}
