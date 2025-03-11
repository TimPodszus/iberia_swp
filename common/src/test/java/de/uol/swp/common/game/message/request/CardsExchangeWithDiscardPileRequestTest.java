package de.uol.swp.common.game.message.request;

import de.uol.swp.common.cards.data.ICardDTO;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CardsExchangeWithDiscardPileRequestTest {

    @Test
    public void testConstructor() {
        Map<String, ICardDTO> cardsToExchange = new HashMap<>();
        String lobbyId = "test-lobby-id";

        CardsExchangeWithDiscardPileRequest request = new CardsExchangeWithDiscardPileRequest(cardsToExchange, lobbyId);

        assertNotNull(request);
        assertEquals(cardsToExchange, request.getCardsToExchange());
        assertEquals(lobbyId, request.getLobbyId());
    }
}