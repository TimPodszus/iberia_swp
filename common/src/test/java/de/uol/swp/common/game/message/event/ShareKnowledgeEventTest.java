package de.uol.swp.common.game.message.event;

import de.uol.swp.common.cards.data.ICardDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class ShareKnowledgeEventTest {

    @Test
    void testShareKnowledgeEvent() {
        String lobbyId = "lobby123";
        String currentPlayer = "player1";
        String targetPlayer = "player2";
        ICardDTO cityCard = mock(ICardDTO.class);

        ShareKnowledgeEvent event = new ShareKnowledgeEvent(lobbyId, currentPlayer, targetPlayer, cityCard);

        assertEquals(lobbyId, event.getLobbyId());
        assertEquals(currentPlayer, event.getCurrentPlayer());
        assertEquals(targetPlayer, event.getTargetPlayer());
        assertEquals(cityCard, event.getCityCard());
    }
}