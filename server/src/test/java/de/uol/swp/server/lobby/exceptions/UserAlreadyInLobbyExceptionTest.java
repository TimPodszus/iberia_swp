package de.uol.swp.server.lobby.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAlreadyInLobbyExceptionTest {

    @Test
    void exceptionMessageIsCorrect() {
        UserAlreadyInLobbyException exception = new UserAlreadyInLobbyException("User is already in the lobby");
        assertEquals("User is already in the lobby", exception.getMessage());
    }

    @Test
    void exceptionIsException() {
        UserAlreadyInLobbyException exception = new UserAlreadyInLobbyException("User is already in the lobby");
        assertInstanceOf(Exception.class, exception);
    }

    @Test
    void exceptionWithNullMessage() {
        UserAlreadyInLobbyException exception = new UserAlreadyInLobbyException(null);
        assertNull(exception.getMessage());
    }
}