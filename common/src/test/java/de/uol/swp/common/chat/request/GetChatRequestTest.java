package de.uol.swp.common.chat.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GetChatRequest.
 */
public class GetChatRequestTest {

    /**
     * Tests the constructor of GetChatRequest.
     */
    @Test
    void testConstructor() {
        GetChatRequest getChatRequest = new GetChatRequest("lobbyId");
        assertEquals("lobbyId", getChatRequest.getLobbyId());
    }

    /**
     * Tests the equals method of GetChatRequest for two objects with the same values.
     */
    @Test
    void testEquals() {
        GetChatRequest getChatRequest = new GetChatRequest("lobbyId");
        GetChatRequest getChatRequest2 = new GetChatRequest("lobbyId");
        assertEquals(getChatRequest, getChatRequest2);
    }

    /**
     * Tests the equals method of GetChatRequest for the same object.
     */
    @Test
    void testEquals_SameObject() {
        GetChatRequest getChatRequest = new GetChatRequest("lobbyId");
        assertEquals(getChatRequest, getChatRequest);
    }

    /**
     * Tests the equals method of GetChatRequest for different object types.
     */
    @Test
    void testEquals_DifferentObject() {
        GetChatRequest getChatRequest = new GetChatRequest("lobbyId");
        Object object = new Object();
        assertNotEquals(getChatRequest, object);
    }

    /**
     * Tests the hashCode method of GetChatRequest.
     */
    @Test
    void testHashCode() {
        GetChatRequest getChatRequest = new GetChatRequest("lobbyId");
        GetChatRequest getChatRequest2 = new GetChatRequest("lobbyId");
        assertEquals(getChatRequest.hashCode(), getChatRequest2.hashCode());
    }
}
