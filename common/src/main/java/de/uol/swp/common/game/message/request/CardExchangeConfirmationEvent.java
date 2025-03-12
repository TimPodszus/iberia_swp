package de.uol.swp.common.game.message.request;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameEvent;
import de.uol.swp.common.player.IPlayerDTO;
import lombok.Getter;

import java.util.Objects;

@Getter
public class CardExchangeConfirmationEvent extends AbstractGameEvent {

    boolean isGiveRequest;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CardExchangeConfirmationEvent that = (CardExchangeConfirmationEvent) o;
        return isGiveRequest == that.isGiveRequest && Objects.equals(
                requestingPlayer,
                that.requestingPlayer
        ) && Objects.equals(
                requestingCard,
                that.requestingCard
        ) && Objects.equals(receivingPlayer, that.receivingPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isGiveRequest, requestingPlayer, requestingCard, receivingPlayer);
    }

    IPlayerDTO requestingPlayer;
    ICardDTO requestingCard;
    IPlayerDTO receivingPlayer;

    public CardExchangeConfirmationEvent(
            String lobbyId,
            boolean isGiveRequest,
            IPlayerDTO requestingPlayer,
            ICardDTO requestingCard,
            IPlayerDTO receivingPlayer
    ) {
        super(lobbyId);
        this.isGiveRequest = isGiveRequest;
        this.requestingPlayer = requestingPlayer;
        this.requestingCard = requestingCard;
        this.receivingPlayer = receivingPlayer;


    }

}
