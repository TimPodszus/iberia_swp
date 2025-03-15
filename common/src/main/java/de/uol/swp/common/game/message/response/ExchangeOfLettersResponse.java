package de.uol.swp.common.game.message.response;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ExchangeOfLettersResponse extends AbstractGameResponse {

    Map<String, List<ICardDTO>> availableExchangeCards;
    String requestingPlayer;

    public ExchangeOfLettersResponse(
            String lobbyId,
            Map<String, List<ICardDTO>> availableExchangeCards,
            String requestingPlayer
    ) {
        super(lobbyId, true);
        this.availableExchangeCards = availableExchangeCards;
        this.requestingPlayer = requestingPlayer;
    }
}
