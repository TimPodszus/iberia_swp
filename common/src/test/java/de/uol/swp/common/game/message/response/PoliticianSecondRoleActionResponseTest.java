package de.uol.swp.common.game.message.response;

import de.uol.swp.common.cards.data.ICardDTO;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PoliticianSecondRoleActionResponseTest {

    @Test
    public void testConstructor() {
        String lobbyId = "lobby123";
        Map<String, List<ICardDTO>> cards = Collections.singletonMap("player1", Collections.emptyList());
        String currentPlayer = "player1";

        PoliticianSecondRoleActionResponse response = new PoliticianSecondRoleActionResponse(
                lobbyId,
                cards,
                currentPlayer
        );

        assertEquals(lobbyId, response.getLobbyId());
        assertEquals(cards, response.getCards());
        assertEquals(currentPlayer, response.getCurrentPlayer());
    }
}