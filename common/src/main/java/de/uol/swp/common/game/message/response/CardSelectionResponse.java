package de.uol.swp.common.game.message.response;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.game.CardSelectionType;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class CardSelectionResponse extends AbstractGameResponse {
    private final List<ICardDTO> cards;
    private final boolean dismissible;
    private final CardSelectionType type;

    /**
     * Constructs a new dismissible CardSelectionResponse.
     *
     * @param lobbyId the ID of the lobby
     * @param success the success status of the response
     * @param cards   the list of selected cards
     * @param type    the type of card selection
     */
    public CardSelectionResponse(
            String lobbyId, boolean success, List<ICardDTO> cards, CardSelectionType type
    ) {
        super(lobbyId, success);
        this.cards = cards;
        this.dismissible = true;
        this.type = type;
    }


}
