package de.uol.swp.common.plague.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

@Getter
public class AvailablePlaguesRequest extends AbstractGameRequest {

    private final int cityID;

    public AvailablePlaguesRequest(String lobbyID, int cityID) {
        super(lobbyID);
        this.cityID = cityID;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
