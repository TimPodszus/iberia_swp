package de.uol.swp.common.player.request;

import de.uol.swp.common.game.message.AbstractGameRequest;

import java.util.Objects;

public class GetCardsToSortRequest extends AbstractGameRequest {

    public GetCardsToSortRequest(String lobbyId) {
        super(lobbyId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetCardsToSortRequest that = (GetCardsToSortRequest) o;
        return Objects.equals(getLobbyId(), that.getLobbyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId());
    }
}
