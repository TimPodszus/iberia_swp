package de.uol.swp.common.game.message.event;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameEvent;
import lombok.Getter;

@Getter
public class ShareKnowledgeEvent extends AbstractGameEvent {
    private final String currentPlayer;
    private final String targetPlayer;
    private final ICardDTO currentPlayerCard;
    private final ICardDTO targetPlayerCard;

    public ShareKnowledgeEvent(String lobbyId, String currentPlayer, String targetPlayer, ICardDTO currentPlayerCard, ICardDTO targetPlayerCard) {
        super(lobbyId);
        this.currentPlayer = currentPlayer;
        this.targetPlayer = targetPlayer;
        this.currentPlayerCard = currentPlayerCard;
        this.targetPlayerCard = targetPlayerCard;
    }


}