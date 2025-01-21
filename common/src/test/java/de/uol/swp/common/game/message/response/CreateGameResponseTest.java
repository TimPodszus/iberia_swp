package de.uol.swp.common.game.message.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGameResponseTest {
    private static final String LOBBY_ID = "LobbyCode";
    private static final String DESCRIPTION = "Description";

    /**
     * Tests the creation of a CreateGameResponse object.
     */
    @Test
    void testCreateGameResponse() {
        CreateGameResponse createGameResponse = new CreateGameResponse(LOBBY_ID, true, DESCRIPTION);
        assertEquals(LOBBY_ID, createGameResponse.getLobbyId());
        assertTrue(createGameResponse.isSuccess());
        assertEquals(DESCRIPTION, createGameResponse.getDescription());
    }

    /**
     * Tests the equality of two CreateGameResponse objects with the same values.
     */
    @Test
    void testEquals() {
        CreateGameResponse createGameResponse = new CreateGameResponse(LOBBY_ID, true, DESCRIPTION);
        CreateGameResponse createGameResponse1 = new CreateGameResponse(LOBBY_ID, true, DESCRIPTION);
        assertEquals(createGameResponse, createGameResponse1);
    }

    /**
     * Tests the equality of a CreateGameResponse object with itself.
     */
    @Test
    void testEqualsWithSameObjects() {
        CreateGameResponse createGameResponse = new CreateGameResponse(LOBBY_ID, true, DESCRIPTION);
        assertEquals(createGameResponse, createGameResponse);
    }

    /**
     * Tests the inequality of a CreateGameResponse object with a different type of object.
     */
    @Test
    void testEqualsWithDifferentObjects() {
        CreateGameResponse createGameResponse = new CreateGameResponse(LOBBY_ID, true, DESCRIPTION);
        Object object = new Object();
        assertNotEquals(createGameResponse, object);
    }

    /**
     * Tests the hashCode method of the CreateGameResponse object.
     */
    @Test
    void testHashCode() {
        CreateGameResponse createGameResponse = new CreateGameResponse(LOBBY_ID, true, DESCRIPTION);
        assertEquals(createGameResponse.hashCode(), createGameResponse.hashCode());
    }
}
