package de.uol.swp.common.game.message.event;

import de.uol.swp.common.cards.data.ICardDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the ShareKnowledgeEvent class.
 */
public class ShareKnowledgeEventTest {

    /**
     * Tests the ShareKnowledgeEvent constructor and its getters.
     */
    @Test
    public void testShareKnowledgeEvent() {
        String lobbyId = "testLobby";
        String currentPlayer = "player1";
        String targetPlayer = "player2";
        ICardDTO currentPlayerCard = mock(ICardDTO.class);
        ICardDTO targetPlayerCard = mock(ICardDTO.class);

        ShareKnowledgeEvent event = new ShareKnowledgeEvent(
                lobbyId,
                currentPlayer,
                targetPlayer,
                currentPlayerCard,
                targetPlayerCard
        );

        assertEquals(lobbyId, event.getLobbyId());
        assertEquals(currentPlayer, event.getCurrentPlayer());
        assertEquals(targetPlayer, event.getTargetPlayer());
        assertEquals(currentPlayerCard, event.getCurrentPlayerCard());
        assertEquals(targetPlayerCard, event.getTargetPlayerCard());
    }

    /**
     * Tests that the ShareKnowledgeEvent object is not null after instantiation.
     */
    @Test
    public void testShareKnowledgeEventNotNull() {
        String lobbyId = "testLobby";
        String currentPlayer = "player1";
        String targetPlayer = "player2";
        ICardDTO currentPlayerCard = mock(ICardDTO.class);
        ICardDTO targetPlayerCard = mock(ICardDTO.class);

        ShareKnowledgeEvent event = new ShareKnowledgeEvent(
                lobbyId,
                currentPlayer,
                targetPlayer,
                currentPlayerCard,
                targetPlayerCard
        );

        assertNotNull(event);
    }
}