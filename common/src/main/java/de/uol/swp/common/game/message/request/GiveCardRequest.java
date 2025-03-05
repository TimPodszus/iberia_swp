package de.uol.swp.common.game.message.request;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

@Getter
public class GiveCardRequest extends AbstractGameRequest {
    private final ICardDTO card;
    private final String targetPlayer;
    private final boolean isGiveCardRequest;
    public GiveCardRequest(ICardDTO card, String targetPlayer ,String lobbyId, boolean isGiveCardRequest) {
        super(lobbyId);
        this.card = card;
        this.targetPlayer = targetPlayer;
        this.isGiveCardRequest = isGiveCardRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GiveCardRequest that = (GiveCardRequest) o;
        return isGiveCardRequest == that.isGiveCardRequest && Objects.equals(card, that.card) && Objects.equals(targetPlayer,
                that.targetPlayer
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(card, targetPlayer, isGiveCardRequest);
    }
}
