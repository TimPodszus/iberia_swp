package de.uol.swp.common.game.message.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserLeavedGameRequestTest {

    @Test
    void equalsReturnsTrueForSameLobbyId() {
        UserLeavedGameRequest request1 = new UserLeavedGameRequest("lobby1");
        UserLeavedGameRequest request2 = new UserLeavedGameRequest("lobby1");
        assertEquals(request1, request2);
    }

    @Test
    void equalsReturnsFalseForDifferentLobbyId() {
        UserLeavedGameRequest request1 = new UserLeavedGameRequest("lobby1");
        UserLeavedGameRequest request2 = new UserLeavedGameRequest("lobby2");
        assertNotEquals(request1, request2);
    }

    @Test
    void equalsReturnsFalseForNull() {
        UserLeavedGameRequest request = new UserLeavedGameRequest("lobby1");
        assertNotEquals(request, null);
    }

    @Test
    void equalsReturnsFalseForDifferentClass() {
        UserLeavedGameRequest request = new UserLeavedGameRequest("lobby1");
        Object obj = new Object();
        assertNotEquals(request, obj);
    }

    @Test
    void hashCodeReturnsSameValueForSameLobbyId() {
        UserLeavedGameRequest request1 = new UserLeavedGameRequest("lobby1");
        UserLeavedGameRequest request2 = new UserLeavedGameRequest("lobby1");
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void hashCodeReturnsDifferentValueForDifferentLobbyId() {
        UserLeavedGameRequest request1 = new UserLeavedGameRequest("lobby1");
        UserLeavedGameRequest request2 = new UserLeavedGameRequest("lobby2");
        assertNotEquals(request1.hashCode(), request2.hashCode());
    }
}