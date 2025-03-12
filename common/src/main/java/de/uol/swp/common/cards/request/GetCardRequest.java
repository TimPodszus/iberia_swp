package de.uol.swp.common.cards.request;

import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Objects;

/**
 * Request to get a card by its ID.
 */
@Getter
public class GetCardRequest extends AbstractGameRequest {
    private final int cardId;

    /**
     * Constructs a new GetCardRequest.
     *
     * @param lobbyId the ID of the lobby
     * @param cardId the ID of the card
     */
    public GetCardRequest(String lobbyId, int cardId){
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
        GetCardRequest that = (GetCardRequest) object;
        return Objects.equals(super.getLobbyId(), that.getLobbyId()) && cardId == that.getCardId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getLobbyId(), cardId);
    }
}

