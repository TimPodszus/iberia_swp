package de.uol.swp.server.connection;

import de.uol.swp.server.city.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class Connection
{
    private final int id;
    private final List<City> cities;
    @Setter
    private boolean trainTrack;
    private final boolean trainTrackBuildable;
}
