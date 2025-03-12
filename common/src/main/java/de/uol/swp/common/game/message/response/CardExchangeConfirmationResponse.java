package de.uol.swp.common.game.message.response;

import de.uol.swp.common.game.message.AbstractGameResponse;
import de.uol.swp.common.game.message.request.CardExchangeConfirmationRequest;
import lombok.Getter;

@Getter
public class CardExchangeConfirmationResponse extends AbstractGameResponse {

    CardExchangeConfirmationRequest cardExchangeConfirmationRequest;

    public CardExchangeConfirmationResponse(String lobbyId, boolean success, CardExchangeConfirmationRequest request) {
        super(lobbyId, success);
        this.cardExchangeConfirmationRequest = request;
    }


}
