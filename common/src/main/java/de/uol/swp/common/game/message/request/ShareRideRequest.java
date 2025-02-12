package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

/**
 * Request to share a ride in the game.
 */
@Getter
public class ShareRideRequest extends AbstractGameRequest {
    private final boolean confirmed;
    private final int cityId;

    /**
     * Constructs a new ShareRideRequest.
     *
     * @param lobbyId the ID of the lobby
     * @param cityId  the ID of the city
     */
    public ShareRideRequest(String lobbyId, int cityId) {
        super(lobbyId);
        this.confirmed = true;
        this.cityId = cityId;
    }

    /**
     * Constructs a new ShareRideRequest with the specified lobby ID.
     * The request is not confirmed and the city ID is set to -1.
     *
     * @param lobbyId the ID of the lobby
     */
    public ShareRideRequest(String lobbyId) {
        super(lobbyId);
        this.confirmed = false;
        this.cityId = -1;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ShareRideRequest that = (ShareRideRequest) object;
        return Objects.equals(super.getLobbyId(), that.getLobbyId()) && Objects.equals(this.confirmed,
                that.isConfirmed()
        ) && Objects.equals(this.cityId, that.cityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getLobbyId(), confirmed, cityId);
    }
}
