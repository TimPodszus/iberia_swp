package de.uol.swp.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnsupportedMethodExceptionTest {

    /**
     * Test for creation of the UnsupportedMethodExceptions
     */
    @Test
    void createUnsupportedMethodException() {
        UnsupportedMethodException exception = new UnsupportedMethodException("Test");
        assertEquals("Test", exception.getMessage());
    }
}
