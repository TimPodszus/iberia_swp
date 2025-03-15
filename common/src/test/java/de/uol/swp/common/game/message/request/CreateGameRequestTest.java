package de.uol.swp.common.game.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CreateGameRequest class.
 */
public class CreateGameRequestTest {

    /**
     * Tests the constructor of CreateGameRequest.
     */
    @Test
    void testConstructor() {
        CreateGameRequest createGameRequest = new CreateGameRequest("LobbyCode", 1, null);
        assertEquals("LobbyCode", createGameRequest.getLobbyId());
        assertEquals(1, createGameRequest.getDifficulty());
        assertNull(createGameRequest.getUsers());
    }

    /**
     * Tests the equals method with two identical CreateGameRequest objects.
     */
    @Test
    void testEquals() {
        CreateGameRequest createGameRequest = new CreateGameRequest("LobbyCode", 1, null);
        CreateGameRequest createGameRequest1 = new CreateGameRequest("LobbyCode", 1, null);
        assertEquals(createGameRequest, createGameRequest1);
    }

    /**
     * Tests the equals method with the same CreateGameRequest object.
     */
    @Test
    void testEqualsWithSameObject() {
        CreateGameRequest createGameRequest = new CreateGameRequest("LobbyCode", 1, null);
        assertEquals(createGameRequest, createGameRequest);
    }

    /**
     * Tests the equals method with a different object type.
     */
    @Test
    void testEqualsWithDifferentObject() {
        CreateGameRequest createGameRequest = new CreateGameRequest("LobbyCode", 1, null);
        Object object = new Object();
        assertNotEquals(createGameRequest, object);
    }

    /**
     * Tests the hashCode method of CreateGameRequest.
     */
    @Test
    void testHashCode() {
        CreateGameRequest createGameRequest = new CreateGameRequest("LobbyCode", 1, null);
        assertEquals(createGameRequest.hashCode(), createGameRequest.hashCode());
    }
}
