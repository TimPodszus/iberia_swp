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


    IPlayerDTO givingCardPlayer;
    ICardDTO requestingCard;
    IPlayerDTO receivingCardPlayer;

    /**
     * Constructs a new CardExchangeConfirmationEvent.
     *
     * @param lobbyId             the ID of the lobby
     * @param givingCardPlayer    the player requesting the card exchange
     * @param requestingCard      the card being requested
     * @param receivingCardPlayer the player receiving the card
     */
    public CardExchangeConfirmationEvent(
            String lobbyId,
            IPlayerDTO givingCardPlayer,
            ICardDTO requestingCard,
            IPlayerDTO receivingCardPlayer
    ) {
        super(lobbyId);
        this.givingCardPlayer = givingCardPlayer;
        this.requestingCard = requestingCard;
        this.receivingCardPlayer = receivingCardPlayer;
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
        return Objects.equals(givingCardPlayer, that.givingCardPlayer) && Objects.equals(
                requestingCard,
                that.requestingCard
        ) && Objects.equals(receivingCardPlayer, that.receivingCardPlayer);
    }

    /**
     * Computes the hash code for this event.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(givingCardPlayer, requestingCard, receivingCardPlayer);
    }


}