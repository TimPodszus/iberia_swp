package de.uol.swp.common.connection;

import de.uol.swp.common.city.CityName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
public class ConnectionDTO implements IConnectionDTO, Serializable {
    private final int id;
    private final List<CityName> cityNames;
    private boolean trainTrack;
    private final boolean trainTrackBuildable;
}
