package de.uol.swp.server.connection;

import de.uol.swp.server.city.data.CityName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for the {@link ConnectionRepository}.
 * This class verifies the functionality of creating and retrieving {@link Connection} objects from the repository.
 */
class ConnectionRepositoryTest {
    /**
     * Static instance of the {@link ConnectionRepository} used for testing.
     */
    static ConnectionRepository repository;

    /**
     * Static list of {@link Connection} objects retrieved from the repository.
     */
    static List<Connection> connections;

    /**
     * Initializes the {@link ConnectionRepository} and retrieves all connections before all test methods are executed.
     */
    @BeforeAll
    static void create() {
        repository = new ConnectionRepository();
        connections = repository.getConnections();
    }

    /**
     * Tests that the repository creates all expected connections.
     * Verifies that the size of the connection list matches the expected number of connections.
     */
    @Test
    void testCreateAllConnections() {
        assertEquals(81, connections.size());
    }

    /**
     * Tests the {@link ConnectionRepository#getConnectionByID(int)} method.
     * Verifies that connections with specific IDs can be retrieved correctly, and that their details match expectations.
     */
    @Test
    void testGetConnectionByIDMethod() {
        Connection connection = repository.getConnectionByID(1);
        assertNotNull(connection);
        assertEquals(1, connection.getId());
        assertEquals(List.of(CityName.A_CORUNA, CityName.GIJON), connection.getCityNames());

        Connection connection28 = repository.getConnectionByID(28);
        assertNotNull(connection28);
        assertEquals(28, connection28.getId());
        assertEquals(List.of(CityName.CADIZ, CityName.GIBRALTAR), connection28.getCityNames());
    }
}