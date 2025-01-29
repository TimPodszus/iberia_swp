package de.uol.swp.common.region.request;

import de.uol.swp.common.game.message.AbstractGameRequest;

public class AvailableRegionsRequest extends AbstractGameRequest {
    public AvailableRegionsRequest(String lobbyId) {
        super(lobbyId);
    }

    /**
     * @param o the object to compare with
     */
    @Override
    public boolean equals(Object o) {
        return false;
    }

    /**
     */
    @Override
    public int hashCode() {
        return 0;
    }
}

