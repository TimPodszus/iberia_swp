package de.uol.swp.common.game.message.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShareKnowledgeResponseTest {

    @Test
    public void testShareKnowledgeResponse() {
        String lobbyId = "testLobby";
        boolean success = true;

        ShareKnowledgeResponse response = new ShareKnowledgeResponse(lobbyId, success);

        assertEquals(lobbyId, response.getLobbyId());
        assertEquals(success, response.isSuccess());
    }
}