package de.uol.swp.server.plague.management;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlagueNotFoundExceptionTest {

    @Test
    void exceptionMessageIsCorrect() {
        PlagueNotFoundException exception = new PlagueNotFoundException("Plague not found");
        assertEquals("Plague not found", exception.getMessage());
    }

    @Test
    void exceptionIsException() {
        PlagueNotFoundException exception = new PlagueNotFoundException("Plague not found");
        assertTrue(exception instanceof Exception);
    }
}