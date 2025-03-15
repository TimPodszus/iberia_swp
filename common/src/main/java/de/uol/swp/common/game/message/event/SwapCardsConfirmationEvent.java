package de.uol.swp.common.game.message.event;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameEvent;
import lombok.Getter;

@Getter
public class SwapCardsConfirmationEvent extends AbstractGameEvent {
    String requestingPlayer;
    String targetPlayer;
    ICardDTO requestingPlayerCard;
    ICardDTO otherPlayerCard;

    public SwapCardsConfirmationEvent(
            String lobbyId,
            String requestingPlayer,
            String targetPlayer,
            ICardDTO requestingPlayerCard,
            ICardDTO otherPlayerCard
    ) {
        super(lobbyId);
        this.requestingPlayer = requestingPlayer;
        this.targetPlayer = targetPlayer;
        this.requestingPlayerCard = requestingPlayerCard;
        this.otherPlayerCard = otherPlayerCard;
    }
}
