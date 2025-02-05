package de.uol.swp.common.connection;

import java.util.List;

public interface IConnectionDTO {
    int getId();

    List<String> getCityNames();

    boolean isTrainTrack();

    boolean isTrainTrackBuildable();
}
