package de.uol.swp.server.connection;

import lombok.*;
import de.uol.swp.server.city.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class Connection {
    private final City startCity;
    private final City endCity;
    @Setter
    private boolean hasTrainTrack;
    private final boolean isWaterWay;
    private final boolean canBuildTrainTracks;
    private final boolean isShipRoute;
}
