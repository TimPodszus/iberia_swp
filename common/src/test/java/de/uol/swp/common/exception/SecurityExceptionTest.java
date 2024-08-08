package de.uol.swp.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for the security exception
 *
 * @see de.uol.swp.common.exception.SecurityException
 * @since 2023-05-14
 */
public class SecurityExceptionTest {

    /**
     * Test for creation of the SecurityExceptions
     *
     * This test checks if the message of the SecurityException gets
     * set correctly during the creation of the exception
     *
     * @since 2023-05-14
     */
    @Test
    void createSecurityException() {
        SecurityException exception = new SecurityException("Test");

        assertEquals("Test", exception.getMessage());
    }

}
