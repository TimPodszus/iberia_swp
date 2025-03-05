package de.uol.swp.common.player.message.response;

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
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        CardsToSortResponse that = (CardsToSortResponse) object;
        return Objects.equals(cards, that.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cards);
    }
}
