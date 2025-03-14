package de.uol.swp.common.game.message.request;

import de.uol.swp.common.cards.data.ICardDTO;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CardsExchangeRequestTest {

    @Test
    public void testConstructor() {
        Map<String, ICardDTO> cardsToExchange = new HashMap<>();
        int cityId = 1;
        String playerToTrade = "player1";
        String lobbyId = "test-lobby-id";

        CardsExchangeRequest request = new CardsExchangeRequest(cardsToExchange, cityId, playerToTrade, lobbyId);

        assertNotNull(request);
        assertEquals(cityId, request.getCityId());
        assertEquals(playerToTrade, request.getPlayerToTrade());
        assertEquals(lobbyId, request.getLobbyId());
    }
}