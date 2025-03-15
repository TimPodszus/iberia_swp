package de.uol.swp.common.connection.dto;

import de.uol.swp.common.city.CityName;

import java.util.List;

public interface IConnectionDTO {
    int getId();

    List<CityName> getCityNames();

    boolean isTrainTrack();

    boolean isTrainTrackBuildable();
}
