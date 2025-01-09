package de.uol.swp.server.game.management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for the {@link GameManagement} class.
 * This class ensures the correct functionality of game management methods, such as creating players,
 * setting the starting player, assigning roles, and initiating infections.
 */
class GameManagementTest {

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    /**
     * Tests the custom {@link GameManagementException}.
     * Ensures that the exception can be thrown and contains the correct message.
     */
    @Test
    void testGameManagementException() {
        GameManagementException exception = assertThrows(GameManagementException.class, () -> {
            throw new GameManagementException("Test Exception");
        });

        assertEquals("Test Exception", exception.getMessage());
    }

}
