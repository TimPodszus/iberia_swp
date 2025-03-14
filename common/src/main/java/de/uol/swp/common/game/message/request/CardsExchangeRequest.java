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

    int cityId;
    String playerToTrade;

    /**
     * Constructs a new CardsExchangeRequest.
     *
     * @param cardsToExchange the map of cards to exchange
     * @param lobbyId         the ID of the lobby
     */
    public CardsExchangeRequest(
            Map<String, ICardDTO> cardsToExchange,
            int cityId,
            String playerToTrade,
            String lobbyId
    ) {
        super(lobbyId);
        this.cityId = cityId;
        this.playerToTrade = playerToTrade;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CardsExchangeRequest that = (CardsExchangeRequest) o;
        return cityId == that.cityId && Objects.equals(playerToTrade, that.playerToTrade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityId, playerToTrade);
    }
}