package de.uol.swp.common.game.message.request;

import de.uol.swp.common.cards.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameRequest;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;
@Getter
public class CardsExchangeRequest extends AbstractGameRequest {

    Map<String, ICardDTO> cardsToExchange;

    public CardsExchangeRequest(Map<String, ICardDTO> cardsToExchange, String lobbyCode) {
        super(lobbyCode);
        this.cardsToExchange = cardsToExchange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardsExchangeRequest that = (CardsExchangeRequest) o;
        return Objects.equals(cardsToExchange, that.cardsToExchange) &&
                Objects.equals(getLobbyId(), that.getLobbyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardsToExchange, getLobbyId());
    }
}