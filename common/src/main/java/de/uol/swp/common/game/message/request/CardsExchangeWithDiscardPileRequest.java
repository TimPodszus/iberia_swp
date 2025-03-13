package de.uol.swp.common.game.message.request;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

@Getter
public class CardsExchangeWithDiscardPileRequest extends AbstractGameRequest {

    Map<String, ICardDTO> cardsToExchange;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CardsExchangeWithDiscardPileRequest request = (CardsExchangeWithDiscardPileRequest) o;
        return Objects.equals(cardsToExchange, request.cardsToExchange);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cardsToExchange);
    }

    /**
     * Constructs a new CardsExchangeRequest.
     *
     * @param cardsToExchange the map of cards to exchange
     * @param lobbyId         the ID of the lobby
     */
    public CardsExchangeWithDiscardPileRequest(Map<String, ICardDTO> cardsToExchange, String lobbyId) {
        super(lobbyId);
        this.cardsToExchange = cardsToExchange;

    }
}
