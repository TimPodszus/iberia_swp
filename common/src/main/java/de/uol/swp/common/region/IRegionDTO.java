package de.uol.swp.common.region;

import java.util.List;

public interface IRegionDTO {
    int getId();
    List<String> getSurroundingCities();
    int getWaterTreatments();
    boolean isPreventionMarker();
}

