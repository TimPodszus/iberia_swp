package de.uol.swp.common.game.message.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PositioningRequestTest {

    private PositioningRequest positioningRequest;

    @BeforeEach
    void setUp() {
        positioningRequest = new PositioningRequest("lobby123", 5);
    }

    @Test
    void testConstructorAndGetter() {
        assertEquals("lobby123", positioningRequest.getLobbyId());
        assertEquals(5, positioningRequest.getCityId());
    }

    @Test
    void testEqualsSelf() {
        assertEquals(positioningRequest, positioningRequest);
    }

    @Test
    void testEqualsSameValues() {
        PositioningRequest other = new PositioningRequest("lobby123", 5);
        assertEquals(positioningRequest, other);
    }

    @Test
    void testNotEqualsDifferentCityId() {
        PositioningRequest other = new PositioningRequest("lobby123", 6);
        assertNotEquals(positioningRequest, other);
    }

    @Test
    void testNotEqualsDifferentLobbyCode() {
        PositioningRequest other = new PositioningRequest("lobby124", 5);
        assertNotEquals(positioningRequest, other, "Requests with different lobby codes should not be equal.");
    }

    @Test
    void testNotEqualsNull() {
        assertNotEquals(null, positioningRequest);
    }

    @Test
    void testNotEqualsDifferentClass() {
        Object other = new Object();
        assertNotEquals(positioningRequest, other);
    }

    @Test
    void testHashCode() {
        int expectedHashCode = Objects.hashCode(5);
        assertEquals(expectedHashCode, positioningRequest.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        int hashCode1 = positioningRequest.hashCode();
        int hashCode2 = positioningRequest.hashCode();
        assertEquals(hashCode1, hashCode2);
    }
}
