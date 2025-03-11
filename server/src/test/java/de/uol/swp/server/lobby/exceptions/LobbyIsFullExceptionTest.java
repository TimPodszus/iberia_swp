package de.uol.swp.server.lobby.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyIsFullExceptionTest {

    @Test
    void exceptionMessageIsCorrect() {
        LobbyIsFullException exception = new LobbyIsFullException("Lobby is full");
        assertEquals("Lobby is full", exception.getMessage());
    }

    @Test
    void exceptionIsException() {
        LobbyIsFullException exception = new LobbyIsFullException("Lobby is full");
        assertInstanceOf(Exception.class, exception);
    }

    @Test
    void exceptionWithNullMessage() {
        LobbyIsFullException exception = new LobbyIsFullException(null);
        assertNull(exception.getMessage());
    }
}