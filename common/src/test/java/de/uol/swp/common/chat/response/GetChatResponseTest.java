package de.uol.swp.common.chat.response;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test class for GetChatResponse.
 */
public class GetChatResponseTest {

    /**
     * Tests the constructor of GetChatResponse.
     */
    @Test
    void testConstructor() {
        GetChatResponse getChatResponse = new GetChatResponse("lobbyId", List.of());
        assertEquals("lobbyId", getChatResponse.getLobbyId());
    }

    /**
     * Tests the equals method of GetChatResponse for two identical objects.
     */
    @Test
    void testEquals() {
        GetChatResponse getChatResponse = new GetChatResponse("lobbyId", List.of());
        GetChatResponse getChatResponse2 = new GetChatResponse("lobbyId", List.of());
        assertEquals(getChatResponse, getChatResponse2);
    }

    /**
     * Tests the equals method of GetChatResponse for the same object.
     */
    @Test
    void testEquals_SameObject() {
        GetChatResponse getChatResponse = new GetChatResponse("lobbyId", List.of());
        assertEquals(getChatResponse, getChatResponse);
    }

    /**
     * Tests the equals method of GetChatResponse for different objects.
     */
    @Test
    void testEquals_DifferentObject() {
        GetChatResponse getChatResponse = new GetChatResponse("lobbyId", List.of());
        Object object = new Object();
        assertNotEquals(getChatResponse, object);
    }

    /**
     * Tests the hashCode method of GetChatResponse.
     */
    @Test
    void testHashCode() {
        GetChatResponse getChatResponse = new GetChatResponse("lobbyId", List.of());
        GetChatResponse getChatResponse2 = new GetChatResponse("lobbyId", List.of());
        assertEquals(getChatResponse.hashCode(), getChatResponse2.hashCode());
    }
}
