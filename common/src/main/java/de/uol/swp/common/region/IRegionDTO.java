package de.uol.swp.common.region;

import java.io.Serializable;
import java.util.List;

public interface IRegionDTO extends Serializable {
    int getId();
    List<String> getSurroundingCities();
    int getWaterTreatments();
    boolean isPreventionMarker();
}

