package de.uol.swp.common.player.message.request;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.Objects;

@Getter
public class DrawPlayerCardResponse extends AbstractGameResponse {
    private final ICardDTO card;

    public DrawPlayerCardResponse(String lobbyId, boolean success, String description, ICardDTO card) {
        super(lobbyId, success, description);
        this.card = card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DrawPlayerCardResponse that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), card);
    }
}