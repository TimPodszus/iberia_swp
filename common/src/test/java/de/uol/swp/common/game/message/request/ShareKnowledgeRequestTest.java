package de.uol.swp.common.game.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ShareKnowledgeRequestTest {

    @Test
    void testConstructorAndGetters() {
        String lobbyId = "lobby123";
        String username = "user1";
        ShareKnowledgeRequest request = new ShareKnowledgeRequest(lobbyId, username);

        assertEquals(lobbyId, request.getLobbyId());
        assertEquals(username, request.getUsername());
    }

    @Test
    void testEquals() {
        String lobbyId = "lobby123";
        String username = "user1";
        ShareKnowledgeRequest request1 = new ShareKnowledgeRequest(lobbyId, username);
        ShareKnowledgeRequest request2 = new ShareKnowledgeRequest(lobbyId, username);
        ShareKnowledgeRequest request3 = new ShareKnowledgeRequest("lobby456", username);

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
    }

    @Test
    void testHashCode() {
        String lobbyId = "lobby123";
        String username = "user1";
        ShareKnowledgeRequest request1 = new ShareKnowledgeRequest(lobbyId, username);
        ShareKnowledgeRequest request2 = new ShareKnowledgeRequest(lobbyId, username);

        assertEquals(request1.hashCode(), request2.hashCode());
    }
}