package de.uol.swp.common.game.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class AvailableShareKnowledgePlayersRequestTest {

    @Test
    public void testEquals() {
        AvailableShareKnowledgePlayersRequest request1 = new AvailableShareKnowledgePlayersRequest("lobby1");
        AvailableShareKnowledgePlayersRequest request2 = new AvailableShareKnowledgePlayersRequest("lobby1");
        AvailableShareKnowledgePlayersRequest request3 = new AvailableShareKnowledgePlayersRequest("lobby2");

        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
    }

    @Test
    public void testHashCode() {
        AvailableShareKnowledgePlayersRequest request1 = new AvailableShareKnowledgePlayersRequest("lobby1");
        AvailableShareKnowledgePlayersRequest request2 = new AvailableShareKnowledgePlayersRequest("lobby1");
        AvailableShareKnowledgePlayersRequest request3 = new AvailableShareKnowledgePlayersRequest("lobby2");

        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }
}