package de.uol.swp.common.game.message.event;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameEvent;
import de.uol.swp.common.player.IPlayerDTO;
import lombok.Getter;

import java.util.Objects;

/**
 * Event representing the confirmation of a card exchange in the game.
 */
@Getter
public class CardExchangeConfirmationEvent extends AbstractGameEvent {

    boolean isGiveRequest;
    IPlayerDTO requestingPlayer;
    ICardDTO requestingCard;
    IPlayerDTO receivingPlayer;

    /**
     * Constructs a new CardExchangeConfirmationEvent.
     *
     * @param lobbyId          the ID of the lobby
     * @param isGiveRequest    whether the request is to give a card
     * @param requestingPlayer the player requesting the card exchange
     * @param requestingCard   the card being requested
     * @param receivingPlayer  the player receiving the card
     */
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

    /**
     * Checks if this event is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
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

    /**
     * Computes the hash code for this event.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(isGiveRequest, requestingPlayer, requestingCard, receivingPlayer);
    }


}