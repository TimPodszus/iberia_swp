package de.uol.swp.server.lobby.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LobbyNotFoundExceptionTest {
    @Test
    void testConstructor() {
        LobbyNotFoundException lobbyNotFoundException = new LobbyNotFoundException("Test");
        assertEquals("Test", lobbyNotFoundException.getMessage());
    }
}
