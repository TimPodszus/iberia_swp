package de.uol.swp.server.cards.management;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardNotPlayableExceptionTest {

    @Test
    void exceptionMessageIsCorrect() {
        CardNotPlayableException exception = new CardNotPlayableException();
        assertEquals("The card is not playable.", exception.getMessage());
    }

    @Test
    void exceptionIsException() {
        CardNotPlayableException exception = new CardNotPlayableException();
        assertTrue(exception instanceof Exception);
    }
}