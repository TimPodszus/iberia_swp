package de.uol.swp.common.game.message.response;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * Response message for card exchange in the game. Contains the cards of possible exchange partners.
 */
@Getter
public class CardExchangeResponse extends AbstractGameResponse {
    /**
     * A map of players and their corresponding cards.
     * Key is the player's username, value is a list of cards.
     */
    private final Map<String, List<ICardDTO>> playerCards;

    /**
     * Constructs a new CardExchangeResponse.
     *
     * @param lobbyId     the ID of the lobby
     * @param success     whether the card exchange was successful
     * @param playerCards a map of players and their corresponding cards
     */
    public CardExchangeResponse(String lobbyId, boolean success, Map<String, List<ICardDTO>> playerCards) {
        super(lobbyId, success);
        this.playerCards = playerCards;
    }
}
