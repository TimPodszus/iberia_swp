package de.uol.swp.common.connectiom;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
public class ConnectionDTO implements IConnectionDTO, Serializable {
    private final int id;
    private final List<String> cityNames;
    private boolean trainTrack;
    private final boolean trainTrackBuildable;
}
