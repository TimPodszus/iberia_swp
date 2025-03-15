package de.uol.swp.server.infection.management;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InfectionManagementExceptionTest {

    @Test
    void testExceptionMessage() {
        String errorMessage = "Test error message";
        InfectionManagementException exception = new InfectionManagementException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}