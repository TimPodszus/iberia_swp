package de.uol.swp.server.game.exception;

import de.uol.swp.server.game.exceptions.GameInitializationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for GameInitializationException.
 */
public class GameInitializationExceptionTest {

    /**
     * Tests the GameInitializationException constructor and getMessage method.
     */
    @Test
    public void testGameInitializationException() {
        GameInitializationException gameInitializationException = new GameInitializationException("Test", new Throwable());
        assertEquals("Test", gameInitializationException.getMessage());
    }
}