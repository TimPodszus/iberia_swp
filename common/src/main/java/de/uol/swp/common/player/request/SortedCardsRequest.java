package de.uol.swp.common.player.request;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameRequest;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class SortedCardsRequest extends AbstractGameRequest {
    private final List<ICardDTO> cards;

    public SortedCardsRequest(String lobbyId, List<ICardDTO> cards) {
        super(lobbyId);
        this.cards = cards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SortedCardsRequest that = (SortedCardsRequest) o;
        return Objects.equals(getLobbyId(), that.getLobbyId()) && Objects.equals(cards, that.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId(), cards);
    }
}
