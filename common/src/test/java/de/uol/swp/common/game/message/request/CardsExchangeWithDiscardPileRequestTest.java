package de.uol.swp.common.game.message.request;

import de.uol.swp.common.cards.data.ICardDTO;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class CardsExchangeWithDiscardPileRequestTest {

    @Test
    public void testConstructor() {
        Map<String, ICardDTO> cardsToExchange = new HashMap<>();
        String lobbyId = "test-lobby-id";

        CardsExchangeWithDiscardPileRequest request = new CardsExchangeWithDiscardPileRequest(cardsToExchange, lobbyId);

        assertNotNull(request);
        assertEquals(lobbyId, request.getLobbyId());
    }

    @Test
    public void testEqualsAndHashCode() {
        Map<String, ICardDTO> cardsToExchange1 = new HashMap<>();
        Map<String, ICardDTO> cardsToExchange2 = new HashMap<>();
        String lobbyId = "test-lobby-id";

        CardsExchangeWithDiscardPileRequest request1 = new CardsExchangeWithDiscardPileRequest(
                cardsToExchange1,
                lobbyId
        );
        CardsExchangeWithDiscardPileRequest request2 = new CardsExchangeWithDiscardPileRequest(
                cardsToExchange2,
                lobbyId
        );

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());

        cardsToExchange2.put("card1", mock(ICardDTO.class));
        CardsExchangeWithDiscardPileRequest request3 = new CardsExchangeWithDiscardPileRequest(
                cardsToExchange2,
                lobbyId
        );

        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
}