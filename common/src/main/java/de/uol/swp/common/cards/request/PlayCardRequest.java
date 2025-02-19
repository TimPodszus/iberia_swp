package de.uol.swp.common.cards.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;


/**
 * A request to play a card in the game.
 */
@Getter
public class PlayCardRequest extends AbstractGameRequest {
    private final int cardId;

    /**
     * Constructs a new PlayCardRequest.
     *
     * @param lobbyId the ID of the lobby
     * @param cardId the ID of the card to be played
     */
    public PlayCardRequest(String lobbyId, int cardId) {
        super(lobbyId);
        this.cardId = cardId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        PlayCardRequest that = (PlayCardRequest) object;
        return super.getLobbyId().equals(that.getLobbyId()) && cardId == that.cardId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getLobbyId(), cardId);
    }
}
