package de.uol.swp.common.player.message.response;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.Objects;

/**
 * A response to a DrawPlayerCardRequest.
 */
@Getter
public class DrawPlayerCardResponse extends AbstractGameResponse {
    /**
     * The card that was drawn.
     */
    private final ICardDTO card;

    /**
     * Constructs a new DrawPlayerCardResponse.
     *
     * @param lobbyId     the ID of the lobby
     * @param success     whether the request was successful
     * @param description a description of the result
     * @param card        the card that was drawn
     */
    public DrawPlayerCardResponse(String lobbyId, boolean success, String description, ICardDTO card) {
        super(lobbyId, success, description);
        this.card = card;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DrawPlayerCardResponse that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), card);
    }
}