package de.uol.swp.common.player.response;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class CardsToSortResponse extends AbstractGameResponse {
    private final List<ICardDTO> cards;

    public CardsToSortResponse(String lobbyId, boolean success, String description, List<ICardDTO> cards) {
        super(lobbyId, success, description);
        this.cards = cards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardsToSortResponse that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(cards, that.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cards);
    }
}
