package de.uol.swp.common.game.message.event;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameEvent;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * Event representing the selection of cards in a game.
 */
@Getter
public class CardSelectionEvent extends AbstractGameEvent {

    private final List<ICardDTO> cards;

    /**
     * Constructs a new CardSelectionEvent.
     *
     * @param lobbyId the ID of the lobby where the event occurred
     * @param cards the list of selected cards
     */
    public CardSelectionEvent(String lobbyId, List<ICardDTO> cards) {
        super(lobbyId);
        this.cards = cards;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        CardSelectionEvent that = (CardSelectionEvent) object;
        return super.equals(that) && Objects.equals(cards, that.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cards);
    }
}
