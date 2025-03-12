package de.uol.swp.common.game.message.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import de.uol.swp.common.game.message.event.CardExchangeConfirmationEvent;
import lombok.Getter;

import java.util.Objects;

@Getter
public class CardExchangeConfirmationRequest extends AbstractGameRequest {

    CardExchangeConfirmationEvent cardExchangeConfirmationRequest;
    boolean accepted;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CardExchangeConfirmationRequest that = (CardExchangeConfirmationRequest) o;
        return Objects.equals(cardExchangeConfirmationRequest, that.cardExchangeConfirmationRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cardExchangeConfirmationRequest);
    }

    public CardExchangeConfirmationRequest(String lobbyId, boolean accepted, CardExchangeConfirmationEvent request) {
        super(lobbyId);
        this.accepted = accepted;
        this.cardExchangeConfirmationRequest = request;
    }


}
