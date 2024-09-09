package de.uol.swp.server.connection;

import de.uol.swp.server.city.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Connection
{
    private final City startCity;
    private final City endCity;
    @Setter
    private boolean trainTrack;
    private final boolean isWaterWay;
    private final boolean trainTrackBuildable;
    private final boolean isShipRoute;
}
