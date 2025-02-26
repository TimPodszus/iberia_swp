package de.uol.swp.common.player.message.event;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameEvent;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * Event representing the request to discard player cards.
 */
@Getter
public class DiscardPlayerCardEvent extends AbstractGameEvent {
    private final List<ICardDTO> cards;

    /**
     * Constructs a new DiscardPlayerCardEvent.
     *
     * @param lobbyId the ID of the lobby
     * @param cards   the list of cards to be discarded
     */
    public DiscardPlayerCardEvent(String lobbyId, List<ICardDTO> cards) {
        super(lobbyId);
        this.cards = cards;
    }

    /**
     * Checks if this event is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiscardPlayerCardEvent that = (DiscardPlayerCardEvent) o;
        return Objects.equals(getLobbyId(), that.getLobbyId()) && Objects.equals(cards, that.cards);
    }

    /**
     * Returns the hash code of this event.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId(), cards);
    }
}