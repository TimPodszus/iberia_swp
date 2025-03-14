package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import de.uol.swp.common.game.message.event.CardExchangeConfirmationEvent;
import lombok.Getter;

import java.util.Objects;

@Getter
public class CardExchangeConfirmationRequest extends AbstractGameRequest {

    CardExchangeConfirmationEvent cardExchangeConfirmationEvent;
    boolean accepted;

    public CardExchangeConfirmationRequest(String lobbyId, boolean accepted, CardExchangeConfirmationEvent event) {
        super(lobbyId);
        this.accepted = accepted;
        this.cardExchangeConfirmationEvent = event;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CardExchangeConfirmationRequest that = (CardExchangeConfirmationRequest) o;
        return Objects.equals(cardExchangeConfirmationEvent, that.cardExchangeConfirmationEvent);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cardExchangeConfirmationEvent);
    }


}
