package de.uol.swp.common.region.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;

/**
 * Request to get available regions for a specific lobby.
 */
public class AvailableRegionsRequest extends AbstractGameRequest {

    /**
     * Constructs a new AvailableRegionsRequest with the specified lobby ID.
     *
     * @param lobbyId the ID of the lobby
     */
    public AvailableRegionsRequest(String lobbyId) {
        super(lobbyId);
    }

    /**
     * Checks if this request is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailableRegionsRequest that = (AvailableRegionsRequest) o;
        return getLobbyId().equals(that.getLobbyId());
    }

    /**
     * Returns the hash code of this request.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return getLobbyId().hashCode();
    }
}