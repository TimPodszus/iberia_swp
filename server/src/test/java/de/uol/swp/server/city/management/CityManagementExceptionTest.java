package de.uol.swp.server.city.management;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CityManagementException.
 */
class CityManagementExceptionTest {

    /**
     * Tests the CityManagementException constructor and getMessage method.
     */
    @Test
    void testCityManagementException() {
        CityManagementException exception = new CityManagementException("Test exception message");
        assertEquals("Test exception message", exception.getMessage());
    }
}