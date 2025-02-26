package de.uol.swp.server.game.exception;

import de.uol.swp.server.game.exceptions.GameNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the GameNotFoundException.
 */
public class GameNotFoundExceptionTest {

    /**
     * Tests the GameNotFoundException constructor and getMessage method.
     */
    @Test
    public void testGameNotFoundException() {
        GameNotFoundException gameNotFoundException = new GameNotFoundException("Test");
        assertEquals("Test", gameNotFoundException.getMessage());
    }
}