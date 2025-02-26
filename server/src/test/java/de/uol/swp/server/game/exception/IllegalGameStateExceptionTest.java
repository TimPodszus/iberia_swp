package de.uol.swp.server.game.exception;

import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for the IllegalGameStateException class.
 */
public class IllegalGameStateExceptionTest {

    /**
     * Tests the IllegalGameStateException constructor and getMessage method.
     * Verifies that the exception message is correctly set and retrieved.
     */
    @Test
    public void testIllegalGameStateException() {
        IllegalGameStateException illegalGameStateException = new IllegalGameStateException("Test");
        assertEquals("Test", illegalGameStateException.getMessage());
    }
}