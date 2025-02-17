package de.uol.swp.common.game.message.request;

import de.uol.swp.common.cards.ICardDTO;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Test class for CardsExchangeRequest.
 */
public class CardsExchangeRequestTest {

    /**
     * Tests the CardsExchangeRequest constructor and its getters.
     */
    @Test
    public void testCardsExchangeRequest() {
        Map<String, ICardDTO> cardsToExchange = new HashMap<>();
        cardsToExchange.put("player1", mock(ICardDTO.class));
        cardsToExchange.put("player2", mock(ICardDTO.class));
        String lobbyCode = "testLobby";

        CardsExchangeRequest request = new CardsExchangeRequest(cardsToExchange, lobbyCode);

        assertEquals(cardsToExchange, request.getCardsToExchange());
        assertEquals(lobbyCode, request.getLobbyId());
    }

    /**
     * Tests the equals and hashCode methods of CardsExchangeRequest.
     */
    @Test
    public void testEqualsAndHashCode() {
        ICardDTO mockCityCard1 = mock(ICardDTO.class);
        ICardDTO mockCityCard2 = mock(ICardDTO.class);

        Map<String, ICardDTO> cardsToExchange1 = new HashMap<>();
        cardsToExchange1.put("player1", mockCityCard1);
        cardsToExchange1.put("player2", mockCityCard2);
        String lobbyCode1 = "testLobby";

        Map<String, ICardDTO> cardsToExchange2 = new HashMap<>();
        cardsToExchange2.put("player1", mockCityCard1);
        cardsToExchange2.put("player2", mockCityCard2);
        String lobbyCode2 = "testLobby";

        CardsExchangeRequest request1 = new CardsExchangeRequest(cardsToExchange1, lobbyCode1);
        CardsExchangeRequest request2 = new CardsExchangeRequest(cardsToExchange2, lobbyCode2);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}