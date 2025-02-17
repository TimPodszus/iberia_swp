package de.uol.swp.common.game.message.event;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameEvent;
import lombok.Getter;

/**
 * Event representing the sharing of knowledge between two players in the game.
 */
@Getter
public class ShareKnowledgeEvent extends AbstractGameEvent {
    private final String currentPlayer;
    private final String targetPlayer;
    private final ICardDTO currentPlayerCard;
    private final ICardDTO targetPlayerCard;

    /**
     * Constructs a new ShareKnowledgeEvent.
     *
     * @param lobbyId           the ID of the lobby where the event occurs
     * @param currentPlayer     the player who is sharing knowledge
     * @param targetPlayer      the player who is receiving knowledge
     * @param currentPlayerCard the card of the current player
     * @param targetPlayerCard  the card of the target player
     */
    public ShareKnowledgeEvent(
            String lobbyId,
            String currentPlayer,
            String targetPlayer,
            ICardDTO currentPlayerCard,
            ICardDTO targetPlayerCard
    ) {
        super(lobbyId);
        this.currentPlayer = currentPlayer;
        this.targetPlayer = targetPlayer;
        this.currentPlayerCard = currentPlayerCard;
        this.targetPlayerCard = targetPlayerCard;
    }
}