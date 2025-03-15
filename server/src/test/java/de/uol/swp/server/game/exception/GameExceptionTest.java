package de.uol.swp.server.game.exception;

import de.uol.swp.server.game.exceptions.GameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the GameException class.
 */
public class GameExceptionTest {

    /**
     * Tests the GameException constructor and getMessage method.
     */
    @Test
    public void testGameException() {
        GameException gameException = new GameException("Test");
        assertEquals("Test", gameException.getMessage());
    }
}