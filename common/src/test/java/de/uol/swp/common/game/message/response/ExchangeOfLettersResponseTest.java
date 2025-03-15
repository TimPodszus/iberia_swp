package de.uol.swp.common.game.message.response;

import de.uol.swp.common.cards.data.ICardDTO;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class ExchangeOfLettersResponseTest {

    @Test
    public void testConstructorAndGetters() {
        String lobbyId = "testLobbyId";
        Map<String, List<ICardDTO>> availableExchangeCards = Map.of(
                "player1",
                List.of(mock(ICardDTO.class)),
                "player2",
                List.of(mock(ICardDTO.class))
        );
        String requestingPlayer = "player1";

        ExchangeOfLettersResponse response = new ExchangeOfLettersResponse(
                lobbyId,
                availableExchangeCards,
                requestingPlayer
        );

        assertEquals(lobbyId, response.getLobbyId());
        assertEquals(availableExchangeCards, response.getAvailableExchangeCards());
        assertEquals(requestingPlayer, response.getRequestingPlayer());
    }
}