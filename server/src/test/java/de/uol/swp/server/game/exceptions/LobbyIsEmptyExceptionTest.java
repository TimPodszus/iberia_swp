package de.uol.swp.server.game.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LobbyIsEmptyExceptionTest {

    @Test
    void exceptionMessageIsCorrect() {
        LobbyIsEmptyException exception = new LobbyIsEmptyException("Lobby is empty");
        assertEquals("Lobby is empty", exception.getMessage());
    }

    @Test
    void exceptionIsInstanceOfException() {
        LobbyIsEmptyException exception = new LobbyIsEmptyException("Lobby is empty");
        assertTrue(exception instanceof Exception);
    }

    @Test
    void exceptionMessageIsNotNull() {
        LobbyIsEmptyException exception = new LobbyIsEmptyException("Lobby is empty");
        assertNotNull(exception.getMessage());
    }

    @Test
    void exceptionMessageIsNotEmpty() {
        LobbyIsEmptyException exception = new LobbyIsEmptyException("Lobby is empty");
        assertFalse(exception.getMessage().isEmpty());
    }
}