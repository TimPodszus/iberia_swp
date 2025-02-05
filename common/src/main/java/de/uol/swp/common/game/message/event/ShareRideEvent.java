package de.uol.swp.common.game.message.event;

import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.message.AbstractGameEvent;
import lombok.Getter;

import java.util.Objects;

/**
 * Request to pick up a player in a specific city.
 */
@Getter
public class ShareRideEvent extends AbstractGameEvent {
    private final ICityDTO city;

    /**
     * Constructs a new PickupPlayerEvent.
     *
     * @param lobbyId the ID of the lobby
     * @param city    the city where the player is to be picked up
     */
    public ShareRideEvent(String lobbyId, ICityDTO city) {
        super(lobbyId);
        this.city = city;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ShareRideEvent that = (ShareRideEvent) object;
        return Objects.equals(super.getLobbyId(), that.getLobbyId()) && Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getLobbyId(), city);
    }
}
