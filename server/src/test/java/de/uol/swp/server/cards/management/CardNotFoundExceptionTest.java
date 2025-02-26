package de.uol.swp.server.cards.management;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardNotFoundExceptionTest {

    /**
     * Tests the constructor of the CardNotFoundException class.
     * Verifies that the message passed to the constructor is correctly set.
     */
    @Test
    void testConstructor() {
        CardNotFoundException exception = new CardNotFoundException("Test");
        assertEquals("Test", exception.getMessage());
    }
}