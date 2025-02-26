package de.uol.swp.common.game.message.request;

import de.uol.swp.common.cards.data.ICardDTO;
import de.uol.swp.common.game.message.event.ShareKnowledgeEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class ShareKnowledgeRequestTest {

    @Test
    public void testShareKnowledgeRequest() {
        String lobbyId = "testLobby";
        boolean success = true;
        String player1 = "player1";
        String player2 = "player2";
        ICardDTO card1 = mock(ICardDTO.class);
        ICardDTO card2 = mock(ICardDTO.class);

        ShareKnowledgeEvent event = new ShareKnowledgeEvent(lobbyId, player1, player2, card1, card2);

        ShareKnowledgeRequest request = new ShareKnowledgeRequest(lobbyId, success, event);

        assertEquals(lobbyId, request.getLobbyId());
        assertEquals(success, request.isSuccess());
        assertEquals(event, request.getShareKnowledgeEvent());
    }

    @Test
    public void testEqualsAndHashCode() {
        String lobbyId = "testLobby";
        boolean success = true;
        String player1 = "player1";
        String player2 = "player2";
        ICardDTO card1 = mock(ICardDTO.class);
        ICardDTO card2 = mock(ICardDTO.class);

        ShareKnowledgeEvent event = new ShareKnowledgeEvent(lobbyId, player1, player2, card1, card2);

        ShareKnowledgeRequest request1 = new ShareKnowledgeRequest(lobbyId, success, event);
        ShareKnowledgeRequest request2 = new ShareKnowledgeRequest(lobbyId, success, event);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}