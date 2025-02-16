package de.uol.swp.common.cards.request;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit tests for the PlayCardRequest class.
 */
public class PlayCardRequestTest {

    /**
     * Tests the constructor of PlayCardRequest.
     */
    @Test
    void testConstructor() {
        PlayCardRequest playCardRequest = new PlayCardRequest("lobbyId", 1);
        assertEquals(1, playCardRequest.getCardId());
        assertEquals("lobbyId", playCardRequest.getLobbyId());
    }

    /**
     * Tests the equals method of PlayCardRequest with two identical objects.
     */
    @Test
    void testEquals() {
        PlayCardRequest playCardRequest = new PlayCardRequest("lobbyId", 1);
        PlayCardRequest playCardRequest2 = new PlayCardRequest("lobbyId", 1);
        assertEquals(playCardRequest, playCardRequest2);
    }

    /**
     * Tests the equals method of PlayCardRequest with the same object.
     */
    @Test
    void testEqualsWithSameObject() {
        PlayCardRequest playCardRequest = new PlayCardRequest("lobbyId", 1);
        assertEquals(playCardRequest, playCardRequest);
    }

    /**
     * Tests the equals method of PlayCardRequest with a different object type.
     */
    @Test
    void testEqualsWithDifferentObject() {
        PlayCardRequest playCardRequest = new PlayCardRequest("lobbyId", 1);
        Object object = new Object();
        assertNotEquals(playCardRequest, object);
    }

    /**
     * Tests the hashCode method of PlayCardRequest.
     */
    @Test
    void testHashCode() {
        PlayCardRequest playCardRequest = new PlayCardRequest("lobbyId", 1);
        assertEquals(Objects.hash("lobbyId", 1), playCardRequest.hashCode());
    }
}