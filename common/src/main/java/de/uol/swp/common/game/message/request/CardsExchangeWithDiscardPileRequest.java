package de.uol.swp.common.game.message.request;

import de.uol.swp.common.cards.data.ICardDTO;

import java.util.Map;

public class CardsExchangeWithDiscardPileRequest extends CardsExchangeRequest {

    boolean playerHasCityCard;

    /**
     * Constructs a new CardsExchangeRequest.
     *
     * @param cardsToExchange the map of cards to exchange
     * @param lobbyId         the ID of the lobby
     */
    public CardsExchangeWithDiscardPileRequest(
            Map<String, ICardDTO> cardsToExchange,
            String lobbyId,
            boolean playerHasCityCard
    ) {
        super(cardsToExchange, lobbyId);
        this.playerHasCityCard = playerHasCityCard;
    }
}
