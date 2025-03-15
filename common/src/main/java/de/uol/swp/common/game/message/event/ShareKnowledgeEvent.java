package de.uol.swp.common.game.message.event;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameEvent;
import lombok.Getter;

/**
 * Event representing the sharing of knowledge between two players in the game.
 */
@Getter
public class ShareKnowledgeEvent extends AbstractGameEvent {
    private final String currentPlayer;
    private final String targetPlayer;
    private final ICardDTO cityCard;


    /**
     * Constructs a new ShareKnowledgeEvent.
     *
     * @param lobbyId       the ID of the lobby where the event occurs
     * @param currentPlayer the player who is sharing knowledge
     * @param targetPlayer  the player who is receiving knowledge
     */
    public ShareKnowledgeEvent(String lobbyId, String currentPlayer, String targetPlayer, ICardDTO cityCard) {
        super(lobbyId);
        this.currentPlayer = currentPlayer;
        this.targetPlayer = targetPlayer;
        this.cityCard = cityCard;

    }
}