package de.uol.swp.common.connectiom;

import java.io.Serializable;
import java.util.List;

public interface IConnectionDTO extends Serializable {
    int getId();

    List<String> getCityNames();

    boolean isTrainTrack();

    boolean isTrainTrackBuildable();
}
