package de.uol.swp.common.game.message.request;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

/**
 * Request message for exchanging cards in a game.
 */
@Getter
public class CardsExchangeRequest extends AbstractGameRequest {

    /**
     * Map of cards to exchange, where the key is a string identifier and the value is the card DTO.
     */
    Map<String, ICardDTO> cardsToExchange;

    /**
     * Constructs a new CardsExchangeRequest.
     *
     * @param cardsToExchange the map of cards to exchange
     * @param lobbyId         the ID of the lobby
     */
    public CardsExchangeRequest(Map<String, ICardDTO> cardsToExchange, String lobbyId) {
        super(lobbyId);
        this.cardsToExchange = cardsToExchange;
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
        CardsExchangeRequest that = (CardsExchangeRequest) o;
        return Objects.equals(cardsToExchange, that.cardsToExchange) && Objects.equals(getLobbyId(), that.getLobbyId());
    }

    /**
     * Computes the hash code for this request.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(cardsToExchange, getLobbyId());
    }
}