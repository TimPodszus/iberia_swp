package de.uol.swp.common.game.message.request;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

/**
 * Request message for exchanging cards in a game.
 */
@Getter
public class CardsExchangeRequest extends AbstractGameRequest {

    String requestingPlayer;
    Map<String, ICardDTO> cardsToExchange;


    /**
     * Constructs a new CardsExchangeRequest.
     *
     * @param cardsToExchange the map of cards to exchange
     * @param lobbyId         the ID of the lobby
     */
    public CardsExchangeRequest(Map<String, ICardDTO> cardsToExchange, String lobbyId, String requestingPlayer) {
        super(lobbyId);
        this.cardsToExchange = cardsToExchange;
        this.requestingPlayer = requestingPlayer;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CardsExchangeRequest request = (CardsExchangeRequest) o;
        return Objects.equals(requestingPlayer, request.requestingPlayer) && Objects.equals(
                cardsToExchange,
                request.cardsToExchange
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestingPlayer, cardsToExchange);
    }


}