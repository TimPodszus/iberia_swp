package de.uol.swp.common.region;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class RegionDTO implements IRegionDTO, Serializable {
    private final int id;
    private final List<String> surroundingCities;
    private int waterTreatments;
    private boolean preventionMarker;
}
