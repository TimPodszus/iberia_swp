package de.uol.swp.server.player;

import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.player.data.IPlayer;

public interface PositionChangeListener {
    void onPositionChanged(IPlayer player, ICity oldPosition, ICity newPosition);
}
