package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;


public class UserLeavedGameRequest extends AbstractGameRequest {
    public UserLeavedGameRequest(String lobbyId) {
        super(lobbyId);
    }

    /**
     * @param o the object to compare with
     * @return
     */
    @Override
    public boolean equals(Object o) {
        return false;
    }

    /**
     * @return
     */
    @Override
    public int hashCode() {
        return 0;
    }
}
