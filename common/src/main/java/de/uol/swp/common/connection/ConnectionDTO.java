package de.uol.swp.common.connection;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ConnectionDTO implements IConnectionDTO {
    private final int id;
    private final List<String> cityNames;
    private boolean trainTrack;
    private final boolean trainTrackBuildable;
}
