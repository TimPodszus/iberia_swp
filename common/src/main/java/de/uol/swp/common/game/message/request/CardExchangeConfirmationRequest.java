package de.uol.swp.common.game.message.request;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameRequest;
import de.uol.swp.common.player.IPlayerDTO;

public class CardExchangeConfirmationRequest extends AbstractGameRequest {

    boolean isGiveRequest;
    IPlayerDTO requestingPlayer;
    ICardDTO requestingCard;

    public CardExchangeConfirmationRequest(
            String lobbyId,
            boolean isGiveRequest,
            IPlayerDTO requestingPlayer,
            ICardDTO requestingCard
    ) {
        super(lobbyId);
        this.isGiveRequest = isGiveRequest;
        this.requestingPlayer = requestingPlayer;
        this.requestingCard = requestingCard;


    }

}
