package de.uol.swp.common.player.message.request;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

/**
 * Request to discard a player card.
 */
@Getter
public class DiscardPlayerCardRequest extends AbstractGameRequest {
    private final ICardDTO card;

    /**
     * Constructs a new DiscardPlayerCardRequest.
     *
     * @param lobbyId the ID of the lobby
     * @param card    the card to be discarded
     */
    public DiscardPlayerCardRequest(String lobbyId, ICardDTO card) {
        super(lobbyId);
        this.card = card;
    }

    /**
     * Checks if this request is equal to another object.
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
        DiscardPlayerCardRequest that = (DiscardPlayerCardRequest) o;
        return Objects.equals(getLobbyId(), that.getLobbyId()) && Objects.equals(card, that.card);
    }

    /**
     * Returns the hash code of this request.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId(), card);
    }
}