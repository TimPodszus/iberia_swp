package de.uol.swp.common.player.request;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

@Getter
public class DiscardPlayerCardRequest extends AbstractGameRequest {
    private final ICardDTO card;

    public DiscardPlayerCardRequest(String lobbyId, ICardDTO card) {
        super(lobbyId);
        this.card = card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiscardPlayerCardRequest that = (DiscardPlayerCardRequest) o;
        return Objects.equals(getLobbyId(), that.getLobbyId()) && Objects.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId(), card);
    }
}
